package com.mfc.settlement.config;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchSchedulerConfig {
	private final JobLauncher jobLauncher;
	private final Job settlementJob;

	@Scheduled(cron = "0 0 0 * * *")
	public void runSettlementJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
		JobRestartException, JobInstanceAlreadyCompleteException {
		JobParameters jobParameters = new JobParametersBuilder()
			.addDate("timestamp", new Date())
			.toJobParameters();
		jobLauncher.run(settlementJob, jobParameters);
	}
}
