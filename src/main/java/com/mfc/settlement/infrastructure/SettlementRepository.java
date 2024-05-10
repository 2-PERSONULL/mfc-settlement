package com.mfc.settlement.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mfc.settlement.domain.Settlement;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
