package org.example.deliveryservice.configuration;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final Key signingKey;

    public JwtTokenUtil(Key signingKey) {
        this.signingKey = signingKey;
    }

    public String generateToken(@NonNull String email) {
        long twentyDaysInMillis = 20 * 24 * 60 * 60 * 1000; // 20 kunni millisekundda hisoblash

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + twentyDaysInMillis))
                .setIssuer("https://www.google.com")
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValid(String token) {
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getEmail(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

}
