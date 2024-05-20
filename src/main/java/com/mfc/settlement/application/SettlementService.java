package com.mfc.settlement.application;

import java.util.List;

import com.mfc.settlement.dto.SettlementResult;


public interface SettlementService {
	void settlePartner(List<? extends SettlementResult> settlementResults);
}
