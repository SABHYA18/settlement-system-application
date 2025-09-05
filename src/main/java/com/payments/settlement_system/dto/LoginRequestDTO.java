package com.payments.settlement_system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class LoginRequestDTO {
    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;
}
