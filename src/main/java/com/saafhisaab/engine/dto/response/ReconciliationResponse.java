package com.saafhisaab.engine.dto.response;

import com.saafhisaab.engine.enums.ReconciliationStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReconciliationResponse {
    private Long id;
    private Long shiftId;
    private Double openingCash;
    private Double totalSales;
    private Double expectedCash;
    private Double actualCash;
    private Double discrepancy;
    private ReconciliationStatus status;
    private LocalDateTime generatedAt;
}