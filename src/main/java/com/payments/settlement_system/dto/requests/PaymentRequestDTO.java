package com.payments.settlement_system.dto.requests;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for handling incoming payment settlement requests from the client.
 * This class defines the structure of the JSON payload the API expects.
 */
@Data
public class PaymentRequestDTO {

    private String payerUsername; // Username of the payer
    private List<PayeeDetails> payees; // List of maps with keys "payeeId" and "amount"

    @Data
    public static class PayeeDetails{
        private String payeeUserName;
        private BigDecimal amount;
    }

}
