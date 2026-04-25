package com.saafhisaab.engine.service.impl;

import com.saafhisaab.engine.dto.request.SaleRequest;
import com.saafhisaab.engine.dto.response.SaleResponse;
import com.saafhisaab.engine.entity.MenuItem;
import com.saafhisaab.engine.entity.Sale;
import com.saafhisaab.engine.entity.Shift;
import com.saafhisaab.engine.entity.User;
import com.saafhisaab.engine.repository.MenuItemRepository;
import com.saafhisaab.engine.repository.SaleRepository;
import com.saafhisaab.engine.repository.ShiftRepository;
import com.saafhisaab.engine.repository.UserRepository;
import com.saafhisaab.engine.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ShiftRepository shiftRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    @Override
    public SaleResponse logSale(SaleRequest request, String mobile) {
        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Shift shift = shiftRepository.findById(request.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));
        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("MenuItem not found"));

        Sale sale = new Sale();
        sale.setShift(shift);
        sale.setMenuItem(menuItem);
        sale.setLoggedBy(user);
        sale.setQuantity(request.getQuantity());
        sale.setUnitPrice(menuItem.getPrice());
        sale.setTotalPrice(menuItem.getPrice() * request.getQuantity());
        sale.setLoggedAt(LocalDateTime.now());

        return mapToResponse(saleRepository.save(sale));
    }

    @Override
    public List<SaleResponse> getSalesByShift(Long shiftId) {
        return saleRepository.findByShiftId(shiftId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SaleResponse mapToResponse(Sale sale) {
        SaleResponse res = new SaleResponse();
        res.setId(sale.getId());
        res.setShiftId(sale.getShift().getId());
        res.setMenuItemName(sale.getMenuItem().getName());
        res.setQuantity(sale.getQuantity());
        res.setUnitPrice(sale.getUnitPrice());
        res.setTotalPrice(sale.getTotalPrice());
        res.setLoggedAt(sale.getLoggedAt());
        return res;
    }
}