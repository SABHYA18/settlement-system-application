package com.payments.settlement_system.controller;

import com.payments.settlement_system.dto.responses.TransactionResponseDTO;
import com.payments.settlement_system.service.settlementsvc.TransactionSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionSvc transactionSvc;

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(Authentication authentication){
       String currentUsername = authentication.getName();

       List<TransactionResponseDTO> transactionHistory = transactionSvc.getTransactionHistory(currentUsername);
       return ResponseEntity.ok(transactionHistory);
    }
}
