package com.payments.settlement_system.controller;

import com.payments.settlement_system.dto.requests.LoginRequestDTO;
import com.payments.settlement_system.dto.responses.LoginResponseDTO;
import com.payments.settlement_system.dto.requests.SignupRequestDTO;
import com.payments.settlement_system.service.securitySvc.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
        try{
            authService.userSignup(signupRequestDTO);
            return ResponseEntity.ok("User sign up complete.");
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
       return authService.userLogin(loginRequestDTO);
    }
}

// now create an endpoint for updating the balance from 0 to the desired value.
