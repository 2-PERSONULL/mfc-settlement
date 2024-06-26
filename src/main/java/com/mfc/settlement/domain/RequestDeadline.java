package com.mfc.settlement.domain;

import java.time.LocalDate;

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
@Table(name = "request_deadline")
@Getter
@NoArgsConstructor
public class RequestDeadline {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long tradeId;

	@Column(nullable = false)
	private LocalDate deadline;

	@Builder
	public RequestDeadline(Long tradeId, LocalDate deadline) {
		this.tradeId = tradeId;
		this.deadline = deadline;
	}
}