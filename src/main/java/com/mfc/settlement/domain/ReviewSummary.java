package com.mfc.settlement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReviewSummary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String partnerId;
	private Double averageRating;
	private Long totalReviews;

	@Builder
	public ReviewSummary(String partnerId, Double averageRating, Long totalReviews) {
		this.partnerId = partnerId;
		this.averageRating = averageRating;
		this.totalReviews = totalReviews;
	}

	public void updateAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public void updateTotalReviews(Long totalReviews) {
		this.totalReviews = totalReviews;
	}
}
