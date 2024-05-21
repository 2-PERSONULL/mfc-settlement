package com.mfc.settlement.dto.request;

import java.time.LocalDateTime;

import com.mfc.settlement.common.entity.SettlementRequestStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementRequestDto {
	private Long partnerId;
	private Integer amount;
	private LocalDateTime settlementDate;
	private Integer feeAmount;
	private String accountNumber;
	private String accountName;
	private SettlementRequestStatus status;
}
