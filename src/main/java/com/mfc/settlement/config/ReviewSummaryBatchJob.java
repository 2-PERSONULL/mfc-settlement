package com.mfc.settlement.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.PlatformTransactionManager;

import com.mfc.settlement.domain.ReviewSummary;
import com.mfc.settlement.dto.kafka.ReviewSummaryDto;
import com.mfc.settlement.infrastructure.ReviewSummaryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ReviewSummaryBatchJob {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final ReviewSummaryRepository reviewSummaryRepository;

	private final Map<String, List<ReviewSummaryDto>> batchData = new ConcurrentHashMap<>();

	@KafkaListener(topics = "create-review", containerFactory = "reviewSummaryKafkaListenerContainerFactory")
	public void receiveReviewSummary(ReviewSummaryDto reviewSummary) {
		log.info("Received review summary for partner: {}", reviewSummary.getPartnerId());
		batchData.computeIfAbsent(reviewSummary.getPartnerId(), k -> new ArrayList<>()).add(reviewSummary);
	}

	@Bean
	public Job reviewSummaryJob() {
		return new JobBuilder("reviewSummaryJob", jobRepository)
			.start(reviewSummaryStep())
			.build();
	}

	@Bean
	public Step reviewSummaryStep() {
		return new StepBuilder("reviewSummaryStep", jobRepository)
			.<String, ReviewSummary>chunk(10, transactionManager)
			.reader(reviewSummaryReader())
			.processor(reviewSummaryProcessor())
			.writer(reviewSummaryWriter())
			.build();
	}

	@Bean
	public ItemReader<String> reviewSummaryReader() {
		return new ItemReader<>() {
			private List<String> partnerIds;
			private int index = 0;

			@Override
			public String read() {
				if (partnerIds == null) {
					partnerIds = new ArrayList<>(batchData.keySet());
					log.info("Starting to process {} partners", partnerIds.size());
				}

				if (index < partnerIds.size()) {
					return partnerIds.get(index++);
				} else {
					partnerIds = null;
					index = 0;
					return null;
				}
			}
		};
	}

	@Bean
	public ItemProcessor<String, ReviewSummary> reviewSummaryProcessor() {
		return partnerId -> {
			List<ReviewSummaryDto> reviews = batchData.get(partnerId);
			if (reviews == null || reviews.isEmpty()) {
				log.warn("No reviews found for partner: {}", partnerId);
				return null;
			}

			ReviewSummary reviewSummary = reviewSummaryRepository.findByPartnerId(partnerId)
				.orElse(ReviewSummary.builder()
					.partnerId(partnerId)
					.totalReviews(0L)
					.averageRating(0.0)
					.build());

			long newTotalReviews = reviewSummary.getTotalReviews() + reviews.size();
			double sumRatings = reviews.stream().mapToDouble(ReviewSummaryDto::getRating).sum();
			double newAverageRating = ((reviewSummary.getAverageRating() * reviewSummary.getTotalReviews()) + sumRatings) / newTotalReviews;

			reviewSummary.updateTotalReviews(newTotalReviews);
			reviewSummary.updateAverageRating(newAverageRating);

			log.info("Processed review summary for partner: {}, total reviews: {}, average rating: {}",
				partnerId, newTotalReviews, newAverageRating);

			return reviewSummary;
		};
	}

	@Bean
	public ItemWriter<ReviewSummary> reviewSummaryWriter() {
		return reviewSummaries -> {
			reviewSummaryRepository.saveAll(reviewSummaries);
			log.info("Saved {} review summaries", reviewSummaries.size());

			// Clear processed data
			reviewSummaries.forEach(summary -> batchData.remove(summary.getPartnerId()));
		};
	}
}