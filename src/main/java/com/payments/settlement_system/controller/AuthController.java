package com.payments.settlement_system.controller;

import ch.qos.logback.core.util.StringUtil;
import com.payments.settlement_system.dto.requests.LoginRequestDTO;
import com.payments.settlement_system.dto.responses.LoginResponseDTO;
import com.payments.settlement_system.dto.requests.SignupRequestDTO;
import com.payments.settlement_system.service.securitySvc.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
            return authService.userSignup(signupRequestDTO);
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
       return ResponseEntity.ok(authService.userLogin(loginRequestDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader)&&authHeader.startsWith("Bearer")){
            String token = authHeader.substring(7);
            authService.logout(token);
            return ResponseEntity.ok("User Logged out successfully");
        }
        return ResponseEntity.badRequest().body("Invalid request: No token provided.");
    }

}

// now create an endpoint for updating the balance from 0 to the desired value.
