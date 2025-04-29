package com.community.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "myVerySecureAndLongJwtSecretKeyForProductionUsemyVerySecureAndLongJwtSecretKeyForProductionUse"; // 256bit 이상 필요
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_16));


    public String generateToken(Long userId, String username, String email) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long extractUserId(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId",Long.class);
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }
    public boolean validateToken(String token, String email) {
        try {
            String extractedEmail = extractEmail(token);
            return (email.equals(extractedEmail) && !isTokenExpired(token));
        } catch (JwtException e) {
            return false; // 토큰 유효하지 않음
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
