package com.payments.settlement_system.service.settlementsvc;

import com.payments.settlement_system.dto.responses.TransactionResponseDTO;
import com.payments.settlement_system.model.Wallet;
import com.payments.settlement_system.repository.TransactionRepository;
import com.payments.settlement_system.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionSvc {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public List<TransactionResponseDTO> getTransactionHistory(String username){
        // 1. Find user's wallet via username
        Wallet wallet = walletRepository.findByUserAccountUsername(username)
                .orElseThrow(()->new IllegalStateException("Wallet not found for user: "+username));

        // 2. Fetch all transactions for the wallet
        return transactionRepository.findByWalletIdOrderByTimestampDesc(wallet.getId())
                .stream()
                .map(TransactionResponseDTO::fromTransaction)
                .collect(Collectors.toList());
    }

}
