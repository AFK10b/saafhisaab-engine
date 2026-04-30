package com.saafhisaab.engine.service.impl;

import com.saafhisaab.engine.dto.response.ReconciliationResponse;
import com.saafhisaab.engine.entity.ReconciliationReport;
import com.saafhisaab.engine.entity.Shift;
import com.saafhisaab.engine.enums.ReconciliationStatus;
import com.saafhisaab.engine.enums.ShiftStatus;
import com.saafhisaab.engine.repository.ReconciliationReportRepository;
import com.saafhisaab.engine.repository.SaleRepository;
import com.saafhisaab.engine.repository.ShiftRepository;
import com.saafhisaab.engine.service.ReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReconciliationServiceImpl implements ReconciliationService {

    private final ShiftRepository shiftRepository;
    private final SaleRepository saleRepository;
    private final ReconciliationReportRepository reconciliationReportRepository;

    private static final double DISCREPANCY_THRESHOLD = 50.0;

    @Override
    public ReconciliationResponse reconcile(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        if (shift.getStatus() != ShiftStatus.CLOSED) {
            throw new RuntimeException("Cannot reconcile an open shift. Close the shift first.");
        }

        // Core reconciliation logic
        double totalSales = saleRepository.findByShiftId(shiftId)
                .stream()
                .mapToDouble(sale -> sale.getTotalPrice())
                .sum();

        double expectedCash = shift.getOpeningCash() + totalSales;
        double actualCash = shift.getClosingCash();
        double discrepancy = expectedCash - actualCash;

        // Determine status
        ReconciliationStatus status;
        if (discrepancy == 0) {
            status = ReconciliationStatus.CLEAR;
        } else if (Math.abs(discrepancy) <= DISCREPANCY_THRESHOLD) {
            status = ReconciliationStatus.WARNING;
        } else {
            status = ReconciliationStatus.ALERT;
        }

        // Save report
        ReconciliationReport report = new ReconciliationReport();
        report.setShift(shift);
        report.setOpeningCash(shift.getOpeningCash());
        report.setTotalSales(totalSales);
        report.setExpectedCash(expectedCash);
        report.setActualCash(actualCash);
        report.setDiscrepancy(discrepancy);
        report.setStatus(status);
        report.setGeneratedAt(LocalDateTime.now());

        return mapToResponse(reconciliationReportRepository.save(report));
    }

    @Override
    public List<ReconciliationResponse> getReportsByVendor(Long vendorId) {
        return reconciliationReportRepository
                .findByShift_Vendor_IdOrderByGeneratedAtDesc(vendorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReconciliationResponse mapToResponse(ReconciliationReport r) {
        ReconciliationResponse res = new ReconciliationResponse();
        res.setId(r.getId());
        res.setShiftId(r.getShift().getId());
        res.setOpeningCash(r.getOpeningCash());
        res.setTotalSales(r.getTotalSales());
        res.setExpectedCash(r.getExpectedCash());
        res.setActualCash(r.getActualCash());
        res.setDiscrepancy(r.getDiscrepancy());
        res.setStatus(r.getStatus());
        res.setGeneratedAt(r.getGeneratedAt());
        return res;
    }
}