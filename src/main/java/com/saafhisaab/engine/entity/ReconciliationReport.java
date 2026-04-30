package com.saafhisaab.engine.entity;

import com.saafhisaab.engine.enums.ReconciliationStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reconciliation_reports")
public class ReconciliationReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false, unique = true)
    private Shift shift;

    @Column(name = "opening_cash", nullable = false)
    private Double openingCash;

    @Column(name = "total_sales", nullable = false)
    private Double totalSales;

    @Column(name = "expected_cash", nullable = false)
    private Double expectedCash;

    @Column(name = "actual_cash", nullable = false)
    private Double actualCash;

    @Column(name = "discrepancy", nullable = false)
    private Double discrepancy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReconciliationStatus status;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;
}