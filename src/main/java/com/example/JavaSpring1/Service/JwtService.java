package com.example.JavaSpring1.Service;

import com.example.JavaSpring1.Config.JwtConfig;
import com.example.JavaSpring1.ENUM.Role;
import com.example.JavaSpring1.Entity.User;
import io.jsonwebtoken.Claims;
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
    public String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtConfig.getExpiration());
        return Jwts.builder().
                setSubject(user.getEmail()).
                claim("role", user.getRole()).
                setIssuedAt(now).
                setExpiration(expirationDate).
                signWith(getSingingKey(), SignatureAlgorithm.HS256).
                compact();
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSingingKey()).build().parseClaimsJws(token).getBody();
    }
     public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
     }
     public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
     }
}
