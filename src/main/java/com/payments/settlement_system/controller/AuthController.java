package com.payments.settlement_system.controller;

import ch.qos.logback.core.util.StringUtil;
import com.payments.settlement_system.dto.requests.LoginRequestDTO;
import com.payments.settlement_system.dto.responses.LoginResponseDTO;
import com.payments.settlement_system.dto.requests.SignupRequestDTO;
import com.payments.settlement_system.service.securitySvc.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
        Map<String, Object> response = new HashMap<>();
        try{
            authService.userSignup(signupRequestDTO);
            response.put("message", "User registered successfully and wallet created!");
            response.put("status", "success");
            response.put("statusCode", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e){
            response.put("message", e.getMessage());
            response.put("status", "error");
            response.put("statusCode", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        Map<String, String> response = new HashMap<>();
        response.put("message", "User Logged in successfully");
        response.put("status", "success");
        response.put("token", authService.userLogin(loginRequestDTO).getToken());
       return ResponseEntity.ok(response);
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


