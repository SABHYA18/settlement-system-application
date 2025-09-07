package com.payments.settlement_system.service.settlementsvc;

import com.payments.settlement_system.dto.requests.BalanceRequestDTO;
import com.payments.settlement_system.dto.responses.BalanceResponseDTO;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceSvc {

    private final UserAccountRepository userAccountRepository;

    @Transactional
    public BalanceResponseDTO addBalance(BigDecimal amountToAdd, String username){
        UserAccount userAccount = userAccountRepository.findById(username)
                .orElseThrow(()->new IllegalStateException("User account not found in the list"));

        BigDecimal newBalance = userAccount.getBalance().add(amountToAdd);
        userAccount.setBalance(newBalance);

        UserAccount updatedAccount = userAccountRepository.save(userAccount);
        return BalanceResponseDTO.fromEntity(userAccount);
    }

    public UserAccount getUserBalance(String userName){
        return userAccountRepository.findById(userName)
                .orElseThrow(()->new IllegalStateException("User account "+userName+" not found."));

    }

}
