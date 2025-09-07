package com.payments.settlement_system.dto.responses;

import com.payments.settlement_system.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.cglib.core.Local;

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

    public static BalanceResponseDTO fromEntity(UserAccount userAccount){
        return BalanceResponseDTO.builder()
                .updatedBalance(userAccount.getBalance())
                .name(userAccount.getName())
                .username(userAccount.getUsername())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
