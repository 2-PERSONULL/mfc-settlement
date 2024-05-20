package com.mfc.settlement.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.settlement.domain.SettlementRequest;
import com.mfc.settlement.dto.SettlementResult;
import com.mfc.settlement.infrastructure.SettlementRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {
	private final SettlementRequestRepository settlementRequestRepository;

	@Transactional
	public void settlePartner(List<? extends SettlementResult> settlementResults) {
		settlementResults.forEach(settlementResult -> {
			// 파트너 정산 처리 로직 구현
			// ...

			SettlementRequest settlementRequest = settlementRequestRepository.findById(settlementResult.getId())
				.orElseThrow(() -> new IllegalArgumentException("Settlement request not found"));

			SettlementRequest updatedSettlementRequest = SettlementRequest.builder()
				.id(settlementRequest.getId())
				.partnerId(settlementRequest.getPartnerId())
				.amount(settlementRequest.getAmount())
				.status("SETTLED")
				.build();

			settlementRequestRepository.save(updatedSettlementRequest);
		});
	}
}