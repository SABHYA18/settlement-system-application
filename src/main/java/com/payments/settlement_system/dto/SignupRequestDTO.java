package com.payments.settlement_system.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignupRequestDTO {
    @NotEmpty(message= "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Phone number cannot be empty")
    private String phone_number;

}
