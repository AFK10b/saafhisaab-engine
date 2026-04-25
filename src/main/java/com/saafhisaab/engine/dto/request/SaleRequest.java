package com.saafhisaab.engine.dto.request;

import lombok.Data;

@Data
public class SaleRequest {
    private Long shiftId;
    private Long menuItemId;
    private Integer quantity;
}