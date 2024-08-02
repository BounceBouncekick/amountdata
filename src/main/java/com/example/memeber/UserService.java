package com.example.memeber;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class UserService {

    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    @Autowired
    public UserService(JobLauncher jobLauncher, Job importUserJob) {
        this.jobLauncher = jobLauncher;
        this.importUserJob = importUserJob;
    }

    public void registerMultipleUsers(long count) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addLong("count", (long) count)
                    .toJobParameters();
            jobLauncher.run(importUserJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute job", e);
        }
    }
}