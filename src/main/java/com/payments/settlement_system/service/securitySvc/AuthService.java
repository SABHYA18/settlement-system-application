package com.payments.settlement_system.service.securitySvc;
import com.payments.settlement_system.dto.responses.LoginResponseDTO;
import com.payments.settlement_system.dto.requests.LoginRequestDTO;
import com.payments.settlement_system.dto.requests.SignupRequestDTO;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.repository.UserAccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    /**
     * Registers a new user in the system.
     * @param request The sign-up request containing username and password.
     * @return The newly created UserAccount entity.
     */
    public UserAccount userSignup(SignupRequestDTO signupRequestDTO){
        // check if the username is already taken
        if(userAccountRepository.existsById(signupRequestDTO.getUsername())){
            throw new IllegalStateException("Username "+ signupRequestDTO.getUsername()+" is already taken. Please choose another one.");
        }

        UserAccount newUser = UserAccount.builder()
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .name(signupRequestDTO.getName())
                .phone_number(signupRequestDTO.getPhone_number())
                .balance(BigDecimal.ZERO)
                .build();

        return userAccountRepository.save(newUser);
    }
    /**
     * Authenticates a user and returns a JWT token.
     * @param request The login request containing username and password.
     * @return A DTO containing the JWT token.
     */

    public LoginResponseDTO userLogin(LoginRequestDTO loginRequestDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );
        // proceed to token generation after authentication
        UserDetails userDetails = userAccountRepository.findById(loginRequestDTO.getUsername())
                .orElseThrow(()->new IllegalStateException("User not found after authentication. This should not happen"));

        String jwtToken = jwtService.generateToken(userDetails);
        return new LoginResponseDTO(jwtToken);
    }

}
