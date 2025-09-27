package com.payments.settlement_system.controller;


import com.payments.settlement_system.dto.requests.PaymentRequestDTO;
import com.payments.settlement_system.repository.WalletRepository;
import com.payments.settlement_system.service.settlementsvc.SettlementSvc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet/settlements")
@RequiredArgsConstructor
public class SettlementController {
    private final SettlementSvc settlementSvc;
    private final WalletRepository walletRepository;

    @PostMapping("/split-settlement")
    public ResponseEntity<String> splitSettlement(@Valid @RequestBody PaymentRequestDTO request, Authentication authentication){
        String currentUsername = authentication.getName();
        if (!currentUsername.equals(request.getPayerUsername())) {
            return ResponseEntity.status(403).body("Forbidden: You can only make payments from your own account.");
        }
        try{
            settlementSvc.splitSettlementRequest(request);
            return ResponseEntity.ok("Settlement processed successfully.");
        } catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(500).body("An unexpected error occurred: "+e.getMessage());
        }
    }

}
