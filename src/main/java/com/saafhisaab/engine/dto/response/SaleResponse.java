package com.saafhisaab.engine.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SaleResponse {
    private Long id;
    private Long shiftId;
    private String menuItemName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private LocalDateTime loggedAt;
}