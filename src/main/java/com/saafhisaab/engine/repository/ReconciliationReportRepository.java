package com.saafhisaab.engine.repository;

import com.saafhisaab.engine.entity.ReconciliationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReconciliationReportRepository extends JpaRepository<ReconciliationReport, Long> {
    Optional<ReconciliationReport> findByShiftId(Long shiftId);
    List<ReconciliationReport> findByShift_Vendor_IdOrderByGeneratedAtDesc(Long vendorId);
}