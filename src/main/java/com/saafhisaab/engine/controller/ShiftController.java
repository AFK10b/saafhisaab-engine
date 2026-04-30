package com.saafhisaab.engine.controller;

import com.saafhisaab.engine.dto.request.CloseShiftRequest;
import com.saafhisaab.engine.dto.request.ShiftRequest;
import com.saafhisaab.engine.dto.response.ReconciliationResponse;
import com.saafhisaab.engine.dto.response.ShiftResponse;
import com.saafhisaab.engine.service.ReconciliationService;
import com.saafhisaab.engine.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;
    private final ReconciliationService reconciliationService;

    @PostMapping("/open")
    public ResponseEntity<ShiftResponse> openShift(@RequestBody ShiftRequest request,
                                                   Authentication auth) {
        String mobile = auth.getName(); // gets mobile from JWT
        return ResponseEntity.ok(shiftService.openShift(request, mobile));
    }

    @PostMapping("/close")
    public ResponseEntity<ReconciliationResponse> closeShift(@RequestBody CloseShiftRequest request,
                                                             Authentication auth) {
        String mobile = auth.getName();
        shiftService.closeShift(request, mobile); // ← pass full request object
        return ResponseEntity.ok(reconciliationService.reconcile(request.getShiftId()));
    }

    @GetMapping("/active/{vendorId}")
    public ResponseEntity<ShiftResponse> getActiveShift(@PathVariable Long vendorId) {
        Optional<ShiftResponse> shift = shiftService.getActiveShift(vendorId);
        return shift.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}