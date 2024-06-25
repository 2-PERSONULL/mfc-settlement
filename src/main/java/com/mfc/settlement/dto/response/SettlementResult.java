package com.mfc.settlement.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class SettlementResult {
	private Long id;
	private String partnerId;
	private Double requestedAmount;
	private Double feeAmount;
	private Double settlementAmount;
	private String accountNumber;
	private String accountName;
	private LocalDateTime settlementDate;
}