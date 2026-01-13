package com.example.JavaSpring1.Service;

import com.example.JavaSpring1.Config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;
    private Key getSingingKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtConfig.getExpiration());
        return Jwts.builder().
                setSubject(email).
                setIssuedAt(now).
                setExpiration(expirationDate).
                signWith(getSingingKey(), SignatureAlgorithm.HS256).
                compact();
    }
}
