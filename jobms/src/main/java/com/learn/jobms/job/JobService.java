package com.learn.jobms.job;

import com.learn.jobms.job.dtos.JobWithCompanyDTO;

import java.util.List;

public interface JobService {
    List<Job> findAll();

    List<JobWithCompanyDTO> findAllJobWithCompany();

    void createJob(Job job);

    Job getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean updateJob(Long id, Job updatedJob);

    JobWithCompanyDTO getJobByIdwithCompany(Long id);
}
