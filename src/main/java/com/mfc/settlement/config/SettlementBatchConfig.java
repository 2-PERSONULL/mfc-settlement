package com.mfc.settlement.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.mfc.settlement.application.SettlementService;
import com.mfc.settlement.domain.SettlementRequest;
import com.mfc.settlement.dto.SettlementResult;
import com.mfc.settlement.infrastructure.SettlementRequestRepository;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class SettlementBatchConfig {
	private final SettlementRequestRepository settlementRequestRepository;
	private final SettlementService settlementService;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job settlementJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("settlementJob", jobRepository)
			.start(settlementStep(jobRepository, transactionManager))
			.build();
	}

	@Bean
	public Step settlementStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("settlementStep", jobRepository)
			.allowStartIfComplete(true)
			.tasklet(settlementTasklet(), transactionManager)
			.build();
	}

	@Bean
	public Tasklet settlementTasklet() {
		return (contribution, chunkContext) -> {
			List<SettlementRequest> pendingRequests = settlementRequestRepository.findByStatus("PENDING");

			List<SettlementResult> settlementResults = pendingRequests.stream()
				.map(settlementRequest -> {
					Integer requestedAmount = settlementRequest.getAmount();
					Integer feeAmount = settlementRequest.getFeeAmount();
					Integer settlementAmount = requestedAmount - feeAmount;

					return SettlementResult.builder()
						.id(settlementRequest.getId())
						.partnerId(settlementRequest.getPartnerId())
						.requestedAmount(requestedAmount)
						.feeAmount(feeAmount)
						.settlementAmount(settlementAmount)
						.accountNumber(settlementRequest.getAccountNumber())
						.build();
				})
				.toList();

			settlementService.settlePartner(settlementResults);

			return RepeatStatus.FINISHED;
		};
	}
}