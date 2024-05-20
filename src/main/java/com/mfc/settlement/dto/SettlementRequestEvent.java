package com.mfc.settlement.dto;

import lombok.Getter;

@Getter
public class SettlementRequestEvent {
	private Long partnerId;
	private Integer amount;
	private String accountNumber;
	private Integer feeAmount;
}
