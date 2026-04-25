package com.saafhisaab.engine.dto.request;

import lombok.Data;

@Data
public class ShiftRequest {
    private Long vendorId;
    private Double openingCash;
}