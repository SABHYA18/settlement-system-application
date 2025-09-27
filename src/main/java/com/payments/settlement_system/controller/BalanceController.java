package com.payments.settlement_system.controller;

import com.payments.settlement_system.dto.requests.BalanceRequestDTO;
import com.payments.settlement_system.dto.responses.BalanceResponseDTO;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.model.Wallet;
import com.payments.settlement_system.service.settlementsvc.BalanceSvc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/wallet/balance")
public class BalanceController {
    private final BalanceSvc balanceSvc;

    @PostMapping("/add-balance")
    public ResponseEntity<BalanceResponseDTO> addBalance(@Valid @RequestBody BalanceRequestDTO balanceRequestDTO, Authentication auth){
        String currentUsername = auth.getName();
        BalanceResponseDTO balanceResponseDTO =  balanceSvc.addBalance(balanceRequestDTO.getBalance(), currentUsername);
        return ResponseEntity.ok(balanceResponseDTO);
    }

    @GetMapping("/fetch-balance/{userName}")
    public ResponseEntity<?> getBalance(@PathVariable String userName){
        try{
            Wallet wallet = balanceSvc.getUserBalance(userName);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("username", wallet.getUserAccount().getUsername());
            responseBody.put("name", wallet.getUserAccount().getName());
            responseBody.put("balance", wallet.getBalance());

            return ResponseEntity.ok(responseBody);
        }catch (IllegalStateException e){
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(500).body("An unexpected error occurred: "+e.getMessage());
        }
    }
}
