package com.payments.settlement_system.service.securitySvc;
import com.payments.settlement_system.dto.responses.LoginResponseDTO;
import com.payments.settlement_system.dto.requests.LoginRequestDTO;
import com.payments.settlement_system.dto.requests.SignupRequestDTO;
import com.payments.settlement_system.model.BlocklistedToken;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.model.Wallet;
import com.payments.settlement_system.repository.BlocklistedTokenRepository;
import com.payments.settlement_system.repository.UserAccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final BlocklistedTokenRepository blocklistedTokenRepository;

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
        // create new user account
        UserAccount newUser = UserAccount.builder()
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .name(signupRequestDTO.getName())
                .phone_number(signupRequestDTO.getPhone_number())
                .build();

        // create new wallet for the user
        Wallet wallet = Wallet.builder()
                .userAccount(newUser)
                .balance(BigDecimal.ZERO)
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        newUser.setWallet(wallet);  // set wallet to the user
        return userAccountRepository.save(newUser); // save user (and wallet due to cascade)

    }
    /**
     * Authenticates a user and returns a JWT token.
     * @param request The login request containing username and password.
     * @return A DTO containing the JWT token.
     */

    public LoginResponseDTO userLogin(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );
        // fetch user details
        var user = userAccountRepository.findById(loginRequestDTO.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication."));
        // set last login timestamp
        user.setLastLoginTimestamp(LocalDateTime.now());
        userAccountRepository.save(user);
        // generate JWT
        var jwtToken = jwtService.generateToken(user);
        return LoginResponseDTO.builder().token(jwtToken).build();
    }

    /**
     * Invalidates a user's JWT by adding it to the blocklist.
     * This effectively logs the user out from all devices using that token.
     * @param token The JWT from the "Authorization: Bearer " header.
     */
    public void logout(String token) {
        var expiryDate = jwtService.extractExpiration(token);
        var expiryLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(expiryDate.getTime()), ZoneId.systemDefault());

        var blocklistedToken = BlocklistedToken.builder()
                .token(token)
                .expiryDate(expiryLocalDateTime)
                .build();
        blocklistedTokenRepository.save(blocklistedToken);
    }

}
