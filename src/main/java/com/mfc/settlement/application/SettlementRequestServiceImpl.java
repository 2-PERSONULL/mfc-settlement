package com.mfc.settlement.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.settlement.common.entity.SettlementRequestStatus;
import com.mfc.settlement.domain.SettlementRequest;
import com.mfc.settlement.dto.request.SettlementRequestDto;
import com.mfc.settlement.infrastructure.SettlementRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettlementRequestServiceImpl implements SettlementRequestService {

	private final SettlementRequestRepository settlementRequestRepository;

	@Override
	@Transactional
	public void saveSettlementRequest(SettlementRequestDto requestDto) {
		SettlementRequest settlementRequest = SettlementRequest.builder()
			.partnerId(requestDto.getPartnerId())
			.amount(requestDto.getAmount())
			.settlementDate(requestDto.getSettlementDate())
			.feeAmount(requestDto.getFeeAmount())
			.accountNumber(requestDto.getAccountNumber())
			.status(SettlementRequestStatus.PENDING)
			.build();

		settlementRequestRepository.save(settlementRequest);
	}

}
