package com.payments.settlement_system.service.settlementsvc;

import com.payments.settlement_system.dto.requests.BalanceRequestDTO;
import com.payments.settlement_system.dto.responses.BalanceResponseDTO;
import com.payments.settlement_system.model.Transaction;
import com.payments.settlement_system.model.TransactionType;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.model.Wallet;
import com.payments.settlement_system.repository.TransactionRepository;
import com.payments.settlement_system.repository.UserAccountRepository;
import com.payments.settlement_system.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BalanceSvc {

    private final UserAccountRepository userAccountRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;


    @Transactional
    public BalanceResponseDTO addBalance(BigDecimal amountToAdd, String username){

        // 1. find user's wallet via username
        Wallet wallet = walletRepository.findByUserAccountUsername(username)
                .orElseThrow(()->new IllegalStateException("Wallet not found for user: "+username));

        // 2. update the balance
        wallet.setBalance(wallet.getBalance().add(amountToAdd));
        wallet.setLastUpdatedAt(LocalDateTime.now());
        Wallet updatedWallet = walletRepository.save(wallet);

        // 3. Create a transaction record (omitted for brevity, but should be implemented)
        Transaction depositTransaction = Transaction.builder()
                .wallet(updatedWallet)
                .type(TransactionType.DEPOSIT)
                .amount(amountToAdd)
                .description("Funds deposited to the wallet")
                .timestamp(LocalDateTime.now())
                .build();
        transactionRepository.save(depositTransaction);

        // 4. Return the updated balance info
        return BalanceResponseDTO.fromWallet(updatedWallet);

    }

    public Wallet getUserBalance(String userName){
        return walletRepository.findByUserAccountUsername(userName)
                .orElseThrow(()->new IllegalStateException("Wallet not found for user: "+userName));

    }

}
