package com.mfc.settlement.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "settlement")
public class Settlement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "partner_id", nullable = false)
	private Long partnerId;

	@Column(name = "settlement_amount", nullable = false)
	private Integer settlementAmount;

	@Column(name = "settlement_date", nullable = false)
	private LocalDateTime settlementDate;

	@Column(name = "fee_amount", nullable = false)
	private Integer feeAmount;

	@Builder
	public Settlement(Long partnerId, Integer settlementAmount, LocalDateTime settlementDate, Integer feeAmount) {
		this.partnerId = partnerId;
		this.settlementAmount = settlementAmount;
		this.settlementDate = settlementDate;
		this.feeAmount = feeAmount;
	}
}
