package com.saafhisaab.engine.service;

import com.saafhisaab.engine.dto.response.ReconciliationResponse;
import java.util.List;

public interface ReconciliationService {
    ReconciliationResponse reconcile(Long shiftId);
    List<ReconciliationResponse> getReportsByVendor(Long vendorId);
}