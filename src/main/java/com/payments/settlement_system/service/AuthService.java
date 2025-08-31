package com.payments.settlement_system.service;

import com.payments.settlement_system.dto.LoginSignupRequestDTO;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserAccount userSignup(LoginSignupRequestDTO loginSignupRequestDTO){
        if(userAccountRepository.existsById(loginSignupRequestDTO.getUsername())){
            throw new IllegalStateException("Username "+ loginSignupRequestDTO.getUsername()+" is already taken. Please choose another one.");
        }

        UserAccount newUser = UserAccount.builder()
                .username(loginSignupRequestDTO.getUsername())
                .password(passwordEncoder.encode(loginSignupRequestDTO.getPassword()))
                .balance(BigDecimal.ZERO)
                .build();

        return userAccountRepository.save(newUser);
    }

    public UserAccount userLogin(LoginSignupRequestDTO loginSignupRequestDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginSignupRequestDTO.getUsername(),
                        loginSignupRequestDTO.getPassword()
                )
        );
        return userAccountRepository.findById(loginSignupRequestDTO.getUsername())
                .orElseThrow(()->new IllegalStateException("User not found after authentication. Please check your credentials."));
    }

}
