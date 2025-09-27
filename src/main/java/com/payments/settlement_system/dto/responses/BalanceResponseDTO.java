package com.payments.settlement_system.dto.responses;

import com.payments.settlement_system.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponseDTO {
    private String username;
    private String name;
    private BigDecimal updatedBalance;
    private LocalDateTime timestamp;

    public static BalanceResponseDTO fromWallet(Wallet wallet){
        return BalanceResponseDTO.builder()
                .username(wallet.getUserAccount().getUsername())
                .name(wallet.getUserAccount().getName())
                .updatedBalance(wallet.getBalance())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
