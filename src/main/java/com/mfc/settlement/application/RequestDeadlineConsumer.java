package com.mfc.settlement.application;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.settlement.domain.RequestDeadline;
import com.mfc.settlement.dto.kafka.TradeSettledEventDto;
import com.mfc.settlement.infrastructure.RequestDeadlineRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestDeadlineConsumer {

	private final RequestDeadlineRepository requestDeadlineRepository;

	@KafkaListener(topics = "partner-completion", containerFactory = "tradeSettledEventKafkaListenerContainerFactory")
	@Transactional
	public void consumeTradeSettledEvent(TradeSettledEventDto eventDto) {
		log.info("Received TradeSettledEvent: {}", eventDto);

		RequestDeadline requestDeadline = RequestDeadline.builder()
			.tradeId(eventDto.getTradeId())
			.deadline(eventDto.getDueDate())
			.build();

		requestDeadlineRepository.save(requestDeadline);

		log.info("Saved RequestDeadline for tradeId: {}, deadline: {}",
			requestDeadline.getTradeId(), requestDeadline.getDeadline());
	}
}