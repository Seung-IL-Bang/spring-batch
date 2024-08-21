/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.samples.helloworld;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.samples.common.DataSourceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.support.JdbcTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
@Import(DataSourceConfiguration.class)
public class HelloWorldJobConfiguration {

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
	public Job job(JobRepository jobRepository, JdbcTransactionManager transactionManager, Step step, Step thirdStep) {
		return new JobBuilder("job", jobRepository)
				.start(step)
				.next(secondStep(jobRepository, transactionManager)) // 생성자로 직접 주입
				.next(thirdStep) // 스프링이 자동 빈 주입
				.build();
	}

}