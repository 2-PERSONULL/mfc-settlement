package com.mfc.settlement.domain;

import java.time.LocalDateTime;

import com.mfc.settlement.common.entity.BaseTimeEntity;
import com.mfc.settlement.common.entity.SettlementRequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "settlement_request")
public class SettlementRequest extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "partner_id", nullable = false)
	private Long partnerId;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	@Column(name = "settlement_date", nullable = false)
	private LocalDateTime settlementDate;

	@Column(name = "fee_amount", nullable = false)
	private Integer feeAmount;

	@Column(name = "account_number", nullable = false)
	private String accountNumber;

	@Column(name = "account_name", nullable = false)
	private String accountName;

	@Column(name = "status", nullable = false)
	private SettlementRequestStatus status;

	@Builder
	public SettlementRequest(Long id, Long partnerId, Integer amount, LocalDateTime settlementDate, Integer feeAmount
	, String accountNumber, String accountName, SettlementRequestStatus status) {
		this.id = id;
		this.partnerId = partnerId;
		this.amount = amount;
		this.settlementDate = settlementDate;
		this.feeAmount = feeAmount;
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.status = status;
	}
}
