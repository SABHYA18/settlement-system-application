package com.payments.settlement_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum TransactionType {

    DEPOSIT,
    PAYMENT_SENT,
    PAYMENT_RECEIVED;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
}

