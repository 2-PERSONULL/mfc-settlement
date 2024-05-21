package com.mfc.settlement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettlementResult {
	private Long id;
	private Long partnerId;
	private Integer requestedAmount;
	private Integer feeAmount;
	private Integer settlementAmount;
	private String accountNumber;
}