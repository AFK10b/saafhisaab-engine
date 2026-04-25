package com.saafhisaab.engine.controller;

import com.saafhisaab.engine.dto.request.SaleRequest;
import com.saafhisaab.engine.dto.response.SaleResponse;
import com.saafhisaab.engine.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/log")
    public ResponseEntity<SaleResponse> logSale(
            @RequestBody SaleRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(saleService.logSale(request, userDetails.getUsername()));
    }

    @GetMapping("/shift/{shiftId}")
    public ResponseEntity<List<SaleResponse>> getSalesByShift(@PathVariable Long shiftId) {
        return ResponseEntity.ok(saleService.getSalesByShift(shiftId));
    }
}