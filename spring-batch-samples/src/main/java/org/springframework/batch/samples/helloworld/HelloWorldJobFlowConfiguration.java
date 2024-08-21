
package org.springframework.batch.samples.helloworld;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.samples.common.DataSourceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.support.JdbcTransactionManager;


import java.util.List;

@Configuration
@EnableBatchProcessing
@Import(DataSourceConfiguration.class)
public class HelloWorldJobFlowConfiguration {

    private static int FIRST_STEP_IDX = 0;
    private static int NEXT_STEP_START_IDX = FIRST_STEP_IDX + 1;


    @Bean
    public Step step(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("step", jobRepository).tasklet((contribution, chunkContext) -> {
            System.out.println("Hello world!");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean
    public Step secondStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("secondStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("Hello Second Step!");

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step thirdStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("thirdStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {

                    System.out.println("Hello Third Step!");
                    Thread.sleep(1000);
                    System.out.println("Third Step Completed!");

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    public List<Step> stepList(Step step, Step secondStep, Step thirdStep) {
        return List.of(step, secondStep, thirdStep);
    }


    @Bean
    public Job job(JobRepository jobRepository, JdbcTransactionManager transactionManager, List<Step> stepList) {
        if (stepList.isEmpty()) {
            throw new IllegalArgumentException("Step list must contain at least one step");
        }

        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("jobFlow");

        // 첫 번째 Step을 시작으로 추가
        flowBuilder.start(stepList.get(FIRST_STEP_IDX));

        // 나머지 Step들을 순차적으로 연결
        for (int i = NEXT_STEP_START_IDX; i < stepList.size(); i++) {
            flowBuilder.next(stepList.get(i));
        }

        // Flow를 빌드하고 Job에 연결
        Flow flow = flowBuilder.build();

        return new JobBuilder("job", jobRepository)
                .start(flow)
                .end()
                .build();
    }

}