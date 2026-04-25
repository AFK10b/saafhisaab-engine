package com.saafhisaab.engine.repository;

import com.saafhisaab.engine.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByVendorIdAndActiveTrue(Long vendorId);
}