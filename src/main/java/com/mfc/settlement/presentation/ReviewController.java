package com.mfc.settlement.presentation;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mfc.settlement.domain.ReviewSummary;
import com.mfc.settlement.infrastructure.ReviewSummaryRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review-count-rating")
@RequiredArgsConstructor
public class ReviewController {
	private ReviewSummaryRepository reviewSummaryRepository;

	@GetMapping("/summary/{partnerId}")
	public ResponseEntity<ReviewSummary> getReviewSummaryByPartnerId(@PathVariable String partnerId) {
		ReviewSummary reviewSummary = reviewSummaryRepository.findByPartnerId(partnerId)
			.orElseThrow(() -> new ResourceNotFoundException("Review summary not found for partnerId: " + partnerId));
		return ResponseEntity.ok(reviewSummary);
	}
}
