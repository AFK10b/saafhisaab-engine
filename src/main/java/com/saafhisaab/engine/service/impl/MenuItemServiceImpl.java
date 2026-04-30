package com.saafhisaab.engine.service.impl;

import com.saafhisaab.engine.entity.MenuItem;
import com.saafhisaab.engine.repository.MenuItemRepository;
import com.saafhisaab.engine.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    @Override
    public List<MenuItem> getMenuItemsByVendor(Long vendorId) {
        return menuItemRepository.findByVendor_IdAndActiveTrue(vendorId);
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }
}