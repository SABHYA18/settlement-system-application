package com.payments.settlement_system.dto.responses;

import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementResponseDTO {
    private String message;
    private String status;
    private BigDecimal amount;
    private UserAccount userAccount;
}
