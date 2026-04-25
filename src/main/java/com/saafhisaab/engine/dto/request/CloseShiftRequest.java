package com.saafhisaab.engine.dto.request;

import lombok.Data;

@Data
public class CloseShiftRequest {
    private Long shiftId;
    private Double closingCash;
}