package com.learn.reviewms.review;

import java.util.List;
import java.util.Map;

public interface ReviewService {
    List<Review> getAllReviews(Long companyId);
    boolean addReview(Long companyId, Review review);
    Review getReview(Long reviewId);
    boolean updateReview(Long reviewId, Review review);
    boolean deleteReview(Long reviewId);

    List<Review> getCompanyReview(Long companyId);

}
