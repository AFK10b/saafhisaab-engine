package com.saafhisaab.engine.dto.request;

import com.saafhisaab.engine.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String mobile;
    private String password;
    private Role role;
    private Long vendorId;
}