package org.springframework.batch.samples.helloworld;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class HelloWorldJobLauncher {

    public static void main(String[] args) {
        // Load the Spring context
//        ApplicationContext context = new AnnotationConfigApplicationContext(HelloWorldJobConfiguration.class);
//        ApplicationContext context = new AnnotationConfigApplicationContext(HelloWorldJobFlowConfiguration.class);
        ApplicationContext context = new AnnotationConfigApplicationContext(HelloWorldJobFlowOrderByConfiguration.class);

        // Get the JobLauncher bean
        JobLauncher jobLauncher = context.getBean(JobLauncher.class);

        // Get the Job bean
        Job job = context.getBean(Job.class);

        try {
            // Run the job with empty parameters (or you can pass actual parameters)
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters());

            System.out.println("Job Status : " + jobExecution.getStatus());
            System.out.println("Job completed");

            LocalDateTime startTime = jobExecution.getStartTime();
            LocalDateTime endTime = jobExecution.getEndTime();

            long lapsed = ChronoUnit.SECONDS.between(startTime, endTime);
            System.out.println("lapsed = " + lapsed);


        } catch (JobExecutionAlreadyRunningException | JobRestartException
                 | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }


    }
}
