package com.mfc.settlement.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mfc.settlement.application.SettlementRequestService;
import com.mfc.settlement.dto.request.SettlementRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/settlement")
@RequiredArgsConstructor
public class SettlementController {

	private final SettlementRequestService settlementRequestService;

	@PostMapping("/settlement-requests")
	public void saveSettlementRequest(@RequestBody SettlementRequestDto requestDto) {
		settlementRequestService.saveSettlementRequest(requestDto);
	}
}
