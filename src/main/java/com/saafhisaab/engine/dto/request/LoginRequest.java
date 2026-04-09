package com.saafhisaab.engine.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String mobile;
    private String password;
}