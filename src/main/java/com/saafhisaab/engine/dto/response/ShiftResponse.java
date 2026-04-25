package com.saafhisaab.engine.dto.response;

import com.saafhisaab.engine.enums.ShiftStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShiftResponse {
    private Long id;
    private Long vendorId;
    private Double openingCash;
    private Double closingCash;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private ShiftStatus status;
}