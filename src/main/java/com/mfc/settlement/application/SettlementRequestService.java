package com.mfc.settlement.application;

import com.mfc.settlement.dto.request.SettlementRequestDto;

public interface SettlementRequestService {
	void saveSettlementRequest(SettlementRequestDto requestDto);
}
