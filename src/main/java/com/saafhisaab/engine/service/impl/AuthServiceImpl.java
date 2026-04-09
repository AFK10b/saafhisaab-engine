package com.saafhisaab.engine.service.impl;

import com.saafhisaab.engine.dto.request.LoginRequest;
import com.saafhisaab.engine.dto.request.RegisterRequest;
import com.saafhisaab.engine.dto.response.AuthResponse;
import com.saafhisaab.engine.entity.User;
import com.saafhisaab.engine.entity.Vendor;
import com.saafhisaab.engine.repository.UserRepository;
import com.saafhisaab.engine.repository.VendorRepository;
import com.saafhisaab.engine.security.jwt.JwtUtil;
import com.saafhisaab.engine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getMobile(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByMobile(request.getMobile())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getMobile());

        return new AuthResponse(
                token,
                user.getName(),
                user.getRole().name(),
                user.getVendor().getId()
        );
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile already registered");
        }

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        User user = new User();
        user.setName(request.getName());
        user.setMobile(request.getMobile());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setVendor(vendor);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getMobile());

        return new AuthResponse(
                token,
                user.getName(),
                user.getRole().name(),
                vendor.getId()
        );
    }
}