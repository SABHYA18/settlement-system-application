package com.payments.settlement_system.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceRequestDTO {
    @NotNull(message = "Balance is required")
    @Positive(message = "Amount must be a positive number.")
    private BigDecimal balance;
}
