package com.mfc.settlement.dto.request;

import java.time.LocalDateTime;

import com.mfc.settlement.common.entity.SettlementRequestStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementRequestDto {
	private String partnerId;
	private Double amount;
	private LocalDateTime settlementDate;
	private Double feeAmount;
	private String accountNumber;
	private String accountName;
	private SettlementRequestStatus status;
}
