package com.saafhisaab.engine.controller;

import com.saafhisaab.engine.entity.MenuItem;
import com.saafhisaab.engine.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long vendorId) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByVendor(vendorId));
    }

    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemService.save(menuItem));
    }
}