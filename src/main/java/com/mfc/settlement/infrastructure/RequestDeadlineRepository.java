package com.mfc.settlement.infrastructure;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mfc.settlement.domain.RequestDeadline;

public interface RequestDeadlineRepository extends JpaRepository<RequestDeadline, Long> {
	List<RequestDeadline> findByDeadlineBefore(LocalDate now);
}
