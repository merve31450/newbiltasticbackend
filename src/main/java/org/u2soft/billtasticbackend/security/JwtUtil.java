package org.u2soft.billtasticbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private final long JWT_EXPIRATION_MS = 15 * 60 * 1000;  // 15 dakika

    // Uygulama başlarken secret string'i byte[] key'e dönüştür
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 1. JWT oluşturur
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // email gibi
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority()) // "ROLE_ADMIN"
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Token'dan kullanıcı adını (email) çıkartır
    public String extractUsername(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    // 3. Token geçerli mi (süresi geçmiş mi?)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // 4. Token süresi bitmiş mi?
    private boolean isTokenExpired(String token) {
        return parseClaims(token).getBody().getExpiration().before(new Date());
    }

    // 5. JWT çözümleme (parçalama)
    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
