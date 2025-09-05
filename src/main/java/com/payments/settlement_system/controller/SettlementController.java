package com.payments.settlement_system.controller;


import com.payments.settlement_system.dto.requests.PaymentRequestDTO;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.service.settlementsvc.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {
    private final SettlementService settlementService;

    @PostMapping("/split-settlement")
    public ResponseEntity<String> splitSettlement(@RequestBody PaymentRequestDTO request){
        try{
            settlementService.splitSettlementRequest(request);
            return ResponseEntity.ok("Settlement processed successfully.");
        } catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(500).body("An unexpected error occurred: "+e.getMessage());
        }
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getBalance(@PathVariable String userId){
        try{
            UserAccount userAccount = settlementService.getUserBalance(userId);
            return ResponseEntity.ok(userAccount);
        }catch (IllegalStateException e){
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(500).body("An unexpected error occurred: "+e.getMessage());
        }
    }
}

// https://github.com/SABHYA18/settlement-system-application.git