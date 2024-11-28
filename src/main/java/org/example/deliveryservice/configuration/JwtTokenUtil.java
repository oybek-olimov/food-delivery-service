package org.example.deliveryservice.configuration;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    public static final String SECRET_KEY = "A665A45920422F9D417E4867EFDC4FB8A04A1F3FFF1FA07E998E86F7F7A27AE3";
    public static final Key signignKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRET_KEY));

    public String generateToken(@NonNull String email) {
        long twentyDaysInMillis = 20 * 24 * 60 * 60 * 1000; // 20 kunni millisekundda hisoblash

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + twentyDaysInMillis))
                .setIssuer("https://www.google.com")
                .signWith(signignKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaims(String token){
       return Jwts.parserBuilder()
                .setSigningKey(signignKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isValid(String token){
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public String getEmail(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

}
