package com.payments.settlement_system.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

@Service
public class JWTService {

    // always store these things in env file
    private static final String SECRET_KEY = "SECRETKEYMUSTBEATLEAST256BITSSECRETKEYMUSTBEATLEAST256BITS";


    public String extractUsername(String token){
        return extractClaim(token);
    }

    public String generateToken(UserDetails userDetails){


    }

}
