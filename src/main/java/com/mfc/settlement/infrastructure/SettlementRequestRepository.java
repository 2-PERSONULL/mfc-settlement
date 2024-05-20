package com.mfc.settlement.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mfc.settlement.domain.SettlementRequest;

public interface SettlementRequestRepository extends JpaRepository<SettlementRequest, Long> {

	List<SettlementRequest> findByStatus(String status);
}
