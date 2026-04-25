package com.saafhisaab.engine.repository;

import com.saafhisaab.engine.entity.Shift;
import com.saafhisaab.engine.enums.ShiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByVendorIdAndStatus(Long vendorId, ShiftStatus status);
}