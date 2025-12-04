package com.example.Testsecure.Utility;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Change this to env variable in production
    private final Key key = Keys.hmacShaKeyFor("THIS_IS_A_SECRET_KEY_SHOULD_BE_32_BYTES_MINIMUM_1234".getBytes());

    public String generateToken(Long id, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAll(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Long getId(String token) {
        return Long.parseLong(extractAll(token).getSubject());
    }

    public String getRole(String token) {
        return extractAll(token).get("role", String.class);
    }
}
