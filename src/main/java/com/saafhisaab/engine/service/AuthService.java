package com.saafhisaab.engine.service;

import com.saafhisaab.engine.dto.request.LoginRequest;
import com.saafhisaab.engine.dto.request.RegisterRequest;
import com.saafhisaab.engine.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}