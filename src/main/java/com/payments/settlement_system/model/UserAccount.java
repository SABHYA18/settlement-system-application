package com.payments.settlement_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "user_accounts")
@Data
@NoArgsConstructor
public class UserAccount {

    @Id
    private String userId;
    private BigDecimal balance;

    public UserAccount(String userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }
}
