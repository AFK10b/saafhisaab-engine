package com.saafhisaab.engine.service;

import com.saafhisaab.engine.entity.MenuItem;
import java.util.List;

public interface MenuItemService {
    List<MenuItem> getMenuItemsByVendor(Long vendorId);
    MenuItem save(MenuItem menuItem);
}