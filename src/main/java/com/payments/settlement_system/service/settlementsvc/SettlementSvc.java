package com.payments.settlement_system.service.settlementsvc;


import com.payments.settlement_system.dto.requests.PaymentRequestDTO;
import com.payments.settlement_system.model.Transaction;
import com.payments.settlement_system.enums.TransactionType;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.model.Wallet;
import com.payments.settlement_system.repository.TransactionRepository;
import com.payments.settlement_system.repository.UserAccountRepository;
import com.payments.settlement_system.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementSvc {

    private final UserAccountRepository userAccountRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void splitSettlementRequest(PaymentRequestDTO requestDTO){

        // 1. fetch the payer and his wallet via his username
        String payerUserName = requestDTO.getPayerUsername();

        UserAccount payerAccount = userAccountRepository.findById(payerUserName)
                .orElseThrow(()->new IllegalStateException("Payer account "+payerUserName+" not found."));

        Wallet payerWallet = walletRepository.findByUserAccountUsername(payerUserName)
                .orElseThrow(()->new IllegalStateException("Wallet for payer "+payerUserName+" not found."));

        // 2. Calculate the total amount to be paid
        BigDecimal totalAmount = requestDTO.getPayees().stream()
                .map(PaymentRequestDTO.PayeeDetails::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Check if the payer has sufficient funds
        if(payerWallet.getBalance().compareTo(totalAmount)<0){
            throw new IllegalStateException("Insufficient funds in payer account "+payerUserName);
        }

        // 4. Deduct the total amount from the payer's account
        payerWallet.setBalance(payerWallet.getBalance().subtract(totalAmount));
        payerWallet.setLastUpdatedAt(LocalDateTime.now());
        walletRepository.save(payerWallet);
        System.out.println("Deducted: "+totalAmount+" from payer account: "+payerAccount.getUsername()+", New balance: "+payerWallet.getBalance());

        // 5. Set the "PAYMENT_SENT" transaction for the payer
        String sentDescription = "Sent a total of "+totalAmount+" to "+requestDTO.getPayees().size()+" payee(s).";
        Transaction sentTransaction = Transaction.builder()
                .wallet(payerWallet)
                .type(TransactionType.PAYMENT_SENT)
                .amount(totalAmount.negate()) // negative amount for deduction
                .timestamp(LocalDateTime.now())
                .description(sentDescription)
                .build();
        transactionRepository.save(sentTransaction);

        //6. Credit each payee's wallet and set "PAYMENT_RECEIVED" transaction
        for(PaymentRequestDTO.PayeeDetails payeeDetails : requestDTO.getPayees()){
            Wallet payeeWallet = walletRepository.findByUserAccountUsername(payeeDetails.getPayeeUserName())
                    .orElseThrow(()-> new IllegalStateException("Wallet for payee "+payeeDetails.getPayeeUserName()+" not found."));
            payeeWallet.setBalance(payeeWallet.getBalance().add(payeeDetails.getAmount()));
            payeeWallet.setLastUpdatedAt(LocalDateTime.now());
            walletRepository.save(payeeWallet);

            // Set the "PAYMENT_RECEIVED" transaction for the payee
            String receivedDescription = "Received "+payeeDetails.getAmount()+" from "+payerUserName;
            Transaction receivedTransaction = Transaction.builder()
                    .wallet(payeeWallet)
                    .type(TransactionType.PAYMENT_RECEIVED)
                    .amount(payeeDetails.getAmount())
                    .timestamp(LocalDateTime.now())
                    .description(receivedDescription)
                    .build();
            transactionRepository.save(receivedTransaction);
        }

    }

}
