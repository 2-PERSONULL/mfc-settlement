package com.mfc.settlement.config;

import java.time.LocalDate;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.mfc.settlement.domain.RequestDeadline;
import com.mfc.settlement.infrastructure.RequestDeadlineRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class RequestDeadlineBatchConfig {
	private final RequestDeadlineRepository requestDeadlineRepository;
	private final KafkaTemplate<String, Long> kafkaTemplate;

	@Bean
	public Job requestDeadlineJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("requestDeadlineJob", jobRepository)
			.start(requestDeadlineStep(jobRepository, transactionManager))
			.build();
	}

	@Bean
	public Step requestDeadlineStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("requestDeadlineStep", jobRepository)
			.allowStartIfComplete(true)
			.tasklet(requestDeadlineTasklet(), transactionManager)
			.build();
	}

	@Bean
	public Tasklet requestDeadlineTasklet() {
		return (contribution, chunkContext) -> {
			LocalDate now = LocalDate.now();

			// 데드라인이 지난 요청서 조회
			List<RequestDeadline> expiredRequests = requestDeadlineRepository.findByDeadlineBefore(now);

			// 데드라인이 지난 요청서의 tradeId를 Kafka 토픽으로 전송
			if (!expiredRequests.isEmpty()) {
				expiredRequests.forEach(request -> {
					kafkaTemplate.send("expired-requests", request.getTradeId());
					log.info("Sent expired trade ID to Kafka: {}", request.getTradeId());
				});
			}

			return RepeatStatus.FINISHED;
		};
	}
}