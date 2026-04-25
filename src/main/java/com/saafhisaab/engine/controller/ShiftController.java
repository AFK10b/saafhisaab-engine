package com.saafhisaab.engine.controller;

import com.saafhisaab.engine.dto.request.CloseShiftRequest;
import com.saafhisaab.engine.dto.request.ShiftRequest;
import com.saafhisaab.engine.dto.response.ShiftResponse;
import com.saafhisaab.engine.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping("/open")
    public ResponseEntity<ShiftResponse> openShift(
            @RequestBody ShiftRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(shiftService.openShift(request, userDetails.getUsername()));
    }

    @PostMapping("/close")
    public ResponseEntity<ShiftResponse> closeShift(
            @RequestBody CloseShiftRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(shiftService.closeShift(request, userDetails.getUsername()));
    }
}