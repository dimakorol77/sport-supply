package org.example.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import java.util.Map;

@Service
public class JwtSecurityService {
    // Секретный ключ для генерации токена
    private static final String SECRET_KEY = "6yU3AaLTrj/YSKQtYF6yU3/YSKAaLTIv9aRtGxOcU39h7T/aRtGxO+syA=";

    // Метод для получения ключа подписи
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Метод для генерации токена
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Срок действия токена 1 день
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Метод для генерации refresh-токена с дополнительными полями
    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 60)) // Срок действия refresh-токена - 60 дней
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Метод для извлечения имени пользователя из токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Общий метод для извлечения нужного поля из токена
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Метод для извлечения всех claims из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // Используем parserBuilder для более новых версий JJWT
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Метод для проверки валидности токена
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Метод для проверки, истек ли срок действия токена
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
