package com.learn.jobms.job.dtos;

import com.learn.jobms.job.Job;
import com.learn.jobms.job.external.Company;
import com.learn.jobms.job.external.Review;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JobWithCompanyDTO {
    private Job job;

    private Company company;

    private List<Review> review;
}
