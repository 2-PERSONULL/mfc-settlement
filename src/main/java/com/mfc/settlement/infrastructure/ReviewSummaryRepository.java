package com.mfc.settlement.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mfc.settlement.domain.ReviewSummary;

public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Long> {

	Optional<ReviewSummary> findByPartnerId(String partnerId);
}
