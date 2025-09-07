package com.payments.settlement_system.service.settlementsvc;


import com.payments.settlement_system.dto.requests.PaymentRequestDTO;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final UserAccountRepository userAccountRepository;

    @Transactional
    public void splitSettlementRequest(PaymentRequestDTO requestDTO){

        // 1. fetch the payer and validate
        String payerId = requestDTO.getPayerId();
        UserAccount payerAccount = userAccountRepository.findById(payerId)
                .orElseThrow(()->new IllegalStateException("Payer account "+payerId+" not found."));

        // 2. Calculate the total amount to be paid
        BigDecimal totalAmount = requestDTO.getPayees().stream()
                .map(PaymentRequestDTO.PayeeDetails::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Check if the payer has sufficient funds
        if(payerAccount.getBalance().compareTo(totalAmount)<0){
            throw new IllegalStateException("Insufficient funds in payer account "+payerId);
        }

        // 4. Deduct the total amount from the payer's account
        payerAccount.setBalance(payerAccount.getBalance().subtract(totalAmount));
        userAccountRepository.save(payerAccount);
        System.out.println("Deducted: "+totalAmount+" from payer account: "+payerAccount.getUsername()+", New balance: "+payerAccount.getBalance());

        //5. Credit each payee's account
        for(PaymentRequestDTO.PayeeDetails payeeDetails : requestDTO.getPayees()){
            String payeeId = payeeDetails.getPayeeId();
            BigDecimal amount = payeeDetails.getAmount();

            UserAccount payee = userAccountRepository.findById(payeeId)
                    .orElseThrow(()-> new IllegalStateException("Payee account "+payeeId+" not found."));

            payee.setBalance(payee.getBalance().add(amount));
            userAccountRepository.save(payee);
            System.out.println("Credited " + amount + " to " + payee.getUsername() + ". New balance: " + payee.getBalance());
        }

    }

}
