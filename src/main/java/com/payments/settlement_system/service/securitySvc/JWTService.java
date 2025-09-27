package com.payments.settlement_system.service.securitySvc;

import com.payments.settlement_system.model.BlocklistedToken;
import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.repository.BlocklistedTokenRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {
    private final BlocklistedTokenRepository blocklistedTokenRepository;

    // always store these things in env file
    private static final String SECRET_KEY = "M9xK7pY3vB8sR5zJ2gL6wD1oH4qN0eF7tC8uY2bA6vI9jX3kS5mP1rQ4";
private static final String LAST_LOGIN_CLAIM = "lastLogin";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails){

        if(userDetails instanceof UserAccount userAccount && userAccount.getLastLoginTimestamp() != null){
            long lastLoginMillis = userAccount.getLastLoginTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli();
            extractClaims.put(LAST_LOGIN_CLAIM, lastLoginMillis);
        }
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && !isTokenBlacklisted(token)
                && isLastLoginValid(token, userDetails);
    }

    private boolean isLastLoginValid(String token, UserDetails userDetails){
        if(userDetails instanceof UserAccount userAccount && userAccount.getLastLoginTimestamp()!=null){
            final Claims claims = extractAllClaims(token);
            Object lastLoginClaim = claims.get(LAST_LOGIN_CLAIM);

            if(lastLoginClaim instanceof Long){
                Date tokenLastLoginDate = new Date((Long) lastLoginClaim);
                Date userLastLoginDate = Date.from(userAccount.getLastLoginTimestamp().toInstant(ZoneOffset.UTC));
                return !tokenLastLoginDate.before(userLastLoginDate);
            }
            return false;
        }
        return true;
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenBlacklisted(String token){
        return blocklistedTokenRepository.findByToken(token).isPresent();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    Claims are the pieces of information (or "statements") that are carried inside the JWT.
    private Claims extractAllClaims(String token){
    return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
    }

}
