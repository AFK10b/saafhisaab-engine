package com.saafhisaab.engine.repository;

import com.saafhisaab.engine.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByShiftId(Long shiftId);
}