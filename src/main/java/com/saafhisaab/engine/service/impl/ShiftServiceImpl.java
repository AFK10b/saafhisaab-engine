package com.saafhisaab.engine.service.impl;

import com.saafhisaab.engine.dto.request.CloseShiftRequest;
import com.saafhisaab.engine.dto.request.ShiftRequest;
import com.saafhisaab.engine.dto.response.ShiftResponse;
import com.saafhisaab.engine.entity.Shift;
import com.saafhisaab.engine.entity.User;
import com.saafhisaab.engine.entity.Vendor;
import com.saafhisaab.engine.enums.ShiftStatus;
import com.saafhisaab.engine.repository.ShiftRepository;
import com.saafhisaab.engine.repository.UserRepository;
import com.saafhisaab.engine.repository.VendorRepository;
import com.saafhisaab.engine.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    @Override
    public ShiftResponse openShift(ShiftRequest request, String mobile) {
        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        shiftRepository.findByVendor_IdAndStatus(vendor.getId(), ShiftStatus.OPEN)
                .ifPresent(s -> { throw new RuntimeException("A shift is already open for this vendor"); });

        Shift shift = new Shift();
        shift.setVendor(vendor);
        shift.setOpenedBy(user);
        shift.setOpeningCash(request.getOpeningCash());
        shift.setOpenedAt(LocalDateTime.now());
        shift.setStatus(ShiftStatus.OPEN);

        return mapToResponse(shiftRepository.save(shift));
    }

    @Override  // ← add this if missing
    public ShiftResponse closeShift(CloseShiftRequest request, String mobile) {
        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Shift shift = shiftRepository.findById(request.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        shift.setClosedBy(user);
        shift.setClosingCash(request.getClosingCash());
        shift.setClosedAt(LocalDateTime.now());
        shift.setStatus(ShiftStatus.CLOSED);

        return mapToResponse(shiftRepository.save(shift));
    }

    private ShiftResponse mapToResponse(Shift shift) {
        ShiftResponse res = new ShiftResponse();
        res.setId(shift.getId());
        res.setVendorId(shift.getVendor().getId());
        res.setOpeningCash(shift.getOpeningCash());
        res.setClosingCash(shift.getClosingCash());
        res.setOpenedAt(shift.getOpenedAt());
        res.setClosedAt(shift.getClosedAt());
        res.setStatus(shift.getStatus());
        return res;
    }

    @Override
    public Optional<ShiftResponse> getActiveShift(Long vendorId) {
        return shiftRepository.findByVendor_IdAndStatus(vendorId, ShiftStatus.OPEN)
                .map(shift -> ShiftResponse.builder()
                        .id(shift.getId())
                        .openingCash(shift.getOpeningCash())
                        .status(shift.getStatus())
                        .build());
    }


}