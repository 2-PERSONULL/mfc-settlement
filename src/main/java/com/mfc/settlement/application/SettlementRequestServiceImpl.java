package com.mfc.settlement.application;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.settlement.common.entity.SettlementRequestStatus;
import com.mfc.settlement.domain.SettlementRequest;
import com.mfc.settlement.dto.kafka.SettlementCashDto;
import com.mfc.settlement.dto.request.SettlementRequestDto;
import com.mfc.settlement.infrastructure.SettlementRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettlementRequestServiceImpl implements SettlementRequestService {

	private final SettlementRequestRepository settlementRequestRepository;
	private final KafkaTemplate<String, SettlementCashDto> kafkaTemplate;

	private static final String TOPIC = "settlement-cash-requests";

	@Override
	@Transactional
	public void saveSettlementRequest(SettlementRequestDto requestDto) {
		SettlementRequest settlementRequest = getSettlementRequest(requestDto);

		settlementRequestRepository.save(settlementRequest);

		publishToKafka(requestDto);
	}

	private static SettlementRequest getSettlementRequest(SettlementRequestDto requestDto) {
		return SettlementRequest.builder()
			.partnerId(requestDto.getPartnerId())
			.amount(requestDto.getAmount())
			.settlementDate(requestDto.getSettlementDate())
			.feeAmount(requestDto.getFeeAmount())
			.accountNumber(requestDto.getAccountNumber())
			.accountName(requestDto.getAccountName())
			.status(SettlementRequestStatus.PENDING)
			.build();
	}

	private void publishToKafka(SettlementRequestDto requestDto) {
		try {
			SettlementCashDto cashDto = new SettlementCashDto(
				requestDto.getPartnerId(),
				requestDto.getAmount()
			);
			kafkaTemplate.send(TOPIC, cashDto);
		} catch (Exception e) {
			// 예외 처리 로직 추가 (로깅 등)
			throw new RuntimeException("Failed to publish message to Kafka", e);
		}
	}
}