package com.learn.reviewms.review.impl;


import com.learn.reviewms.event.Ratingevent;
import com.learn.reviewms.review.Review;
import com.learn.reviewms.review.ReviewRepository;
import com.learn.reviewms.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final KafkaTemplate<Long, Ratingevent> kafkaTemplate;

    @Value("${kafka.topic.company-rating_topic}")
    private String KAFKA_COMPANY_RATING_TOPIC;


    @Override
    public List<Review> getAllReviews(Long companyId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews;
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if (companyId != null && review != null){
            review.setCompanyId(companyId);
            reviewRepository.save(review);

            Ratingevent rating=new Ratingevent();
            rating.setCompanyId(review.getCompanyId());
            rating.setRating(review.getRating());
            kafkaTemplate.send(KAFKA_COMPANY_RATING_TOPIC,rating.getCompanyId(),rating); //just mentioned key value pair
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null){
            review.setTitle(updatedReview.getTitle());
            review.setDescription(updatedReview.getDescription());
            review.setRating(updatedReview.getRating());
            review.setCompanyId(updatedReview.getCompanyId());
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null){
            reviewRepository.delete(review);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Review> getCompanyReview(Long companyId) {
        List<Review> review=reviewRepository.findByCompanyId(companyId);

        return review;
    }

}
