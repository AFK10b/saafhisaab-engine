package com.saafhisaab.engine.service;

import com.saafhisaab.engine.dto.request.SaleRequest;
import com.saafhisaab.engine.dto.response.SaleResponse;
import java.util.List;

public interface SaleService {
    SaleResponse logSale(SaleRequest request, String mobile);
    List<SaleResponse> getSalesByShift(Long shiftId);
}