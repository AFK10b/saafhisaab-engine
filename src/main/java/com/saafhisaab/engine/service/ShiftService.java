package com.saafhisaab.engine.service;

import com.saafhisaab.engine.dto.request.CloseShiftRequest;
import com.saafhisaab.engine.dto.request.ShiftRequest;
import com.saafhisaab.engine.dto.response.ShiftResponse;
import java.util.Optional;

public interface ShiftService {
    ShiftResponse openShift(ShiftRequest request, String mobile);
    ShiftResponse closeShift(CloseShiftRequest request, String mobile);
    Optional<ShiftResponse> getActiveShift(Long vendorId);
}