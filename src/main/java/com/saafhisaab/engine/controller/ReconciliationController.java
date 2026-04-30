package com.saafhisaab.engine.controller;

import com.saafhisaab.engine.dto.response.ReconciliationResponse;
import com.saafhisaab.engine.service.ReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reconciliation")
@RequiredArgsConstructor
public class ReconciliationController {

    private final ReconciliationService reconciliationService;

    @PostMapping("/shift/{shiftId}")
    public ResponseEntity<ReconciliationResponse> reconcile(@PathVariable Long shiftId) {
        return ResponseEntity.ok(reconciliationService.reconcile(shiftId));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<ReconciliationResponse>> getByVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(reconciliationService.getReportsByVendor(vendorId));
    }
}