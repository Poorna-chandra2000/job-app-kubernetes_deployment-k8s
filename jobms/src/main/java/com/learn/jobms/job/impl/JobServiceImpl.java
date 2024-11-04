package com.learn.jobms.job.impl;



import com.learn.jobms.job.Job;
import com.learn.jobms.job.JobRepository;
import com.learn.jobms.job.JobService;
import com.learn.jobms.job.clients.CompanyClient;
import com.learn.jobms.job.clients.ReviewClient;
import com.learn.jobms.job.dtos.JobWithCompanyDTO;
import com.learn.jobms.job.external.Company;
import com.learn.jobms.job.external.Review;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    //dependency inject
    private final JobRepository jobRepository;

//    private final RestTemplate restTemplate;


    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;


    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }
//Rest template methods...................................................
//    @Override
//    public List<JobWithCompanyDTO> findAllJobWithCompany() {
//
//        List<Job> jobs= jobRepository.findAll();
//        List<JobWithCompanyDTO> jobWithCompanyDTOS=new ArrayList<>();
//
//
//        for (Job job:jobs){
//
//            Company company=restTemplate.getForObject("http://companyms:8081/companies/"+job.getCompanyId(), Company.class);
//            Review[] review=restTemplate.getForObject("http://reviewms:8083/reviews/companyreview/"+job.getCompanyId(), Review[].class);
//            List<Review> reviews= Arrays.asList(review);
//            JobWithCompanyDTO jobWithCompanyDTO=JobWithCompanyDTO.builder()
//                            .job(job)
//                                    .company(company)
//                    .review(reviews)
//                                            .build();
//            jobWithCompanyDTOS.add(jobWithCompanyDTO);
//        }
//     return jobWithCompanyDTOS;
//    }
    //...................................................................

    //Open feign method.................................................
    int attempts=0;
@Override
//@Retry(name="companyBreaker",fallbackMethod = "companyBreakerFallback2")
@RateLimiter(name="companyBreaker",fallbackMethod = "companyBreakerFallback2")
public List<JobWithCompanyDTO> findAllJobWithCompany() {

        log.info("Attemps {}",++attempts);
    List<Job> jobs= jobRepository.findAll();
    List<JobWithCompanyDTO> jobWithCompanyDTOS=new ArrayList<>();


    for (Job job:jobs){
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
        JobWithCompanyDTO jobWithCompanyDTO=JobWithCompanyDTO.builder()
                .job(job)
                .company(company)
                .review(reviews)
                .build();
        jobWithCompanyDTOS.add(jobWithCompanyDTO);
    }
    return jobWithCompanyDTOS;
}
    //must match the type of original service
    //remeber must have same arguments too as above service method
    // Fallback method must match the return type and parameters of the original method
    public List<JobWithCompanyDTO> companyBreakerFallback2(Throwable t) {
        log.error("Fallback triggered due to exception: {}", t.getMessage());

        // Create a fallback response with a generic message
        Company fallbackCompany = new Company();
        fallbackCompany.setName("Company details not available");

        List<Review> fallbackReviews = new ArrayList<>();

        // Construct a default JobWithCompanyDTO for the fallback
        JobWithCompanyDTO fallbackDTO = JobWithCompanyDTO.builder()
                .company(fallbackCompany)
                .job(null) // No job available in fallback
                .review(fallbackReviews)
                .build();

        // Return a list with the fallback DTO
        List<JobWithCompanyDTO> fallbackList = new ArrayList<>();
        fallbackList.add(fallbackDTO);

        return fallbackList;
    }



    @Override
    public void createJob(Job job) {
//        RestTemplate restTemplate=new RestTemplate();
//        Company company=restTemplate.getForObject("http://localHost:8081/companies/1", Company.class);
//        job.setCompany(company.getName());
        jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }


    @Override
    @CircuitBreaker(name="companyBreaker",fallbackMethod = "companyBreakerFallback")
    public JobWithCompanyDTO getJobByIdwithCompany(Long id) {
        Job job= jobRepository.findById(id).orElse(null);
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
       return JobWithCompanyDTO.builder()
               .company(company)
               .job(job)
               .review(reviews)
               .build();
    }

    //must match the type of original service
    //remeber must have same arguments too as above service method
    public JobWithCompanyDTO companyBreakerFallback(Long id, Exception e) {
        // Fallback response when the CircuitBreaker is triggered
        Job fallbackJob = jobRepository.findById(id).orElse(null);
        Company fallbackCompany = new Company();
        fallbackCompany.setName("Company details not available");

        List<Review> fallbackReviews = new ArrayList<>();

        return JobWithCompanyDTO.builder()
                .company(fallbackCompany)
                .job(fallbackJob)
                .review(fallbackReviews)
                .build();
    }


    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setLocation(updatedJob.getLocation());
            jobRepository.save(job);
            return true;
        }
        return false;
    }
}