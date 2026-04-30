package com.saafhisaab.engine.enums;

public enum ReconciliationStatus {
    CLEAR,      // discrepancy = 0
    WARNING,    // discrepancy within threshold (e.g. < 50)
    ALERT       // discrepancy exceeds threshold
}