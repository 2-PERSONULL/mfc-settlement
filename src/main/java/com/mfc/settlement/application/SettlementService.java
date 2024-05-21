package com.mfc.settlement.application;

import java.util.List;

import com.mfc.settlement.dto.response.SettlementResult;


public interface SettlementService {
	void settlePartner(List<? extends SettlementResult> settlementResults);
}
