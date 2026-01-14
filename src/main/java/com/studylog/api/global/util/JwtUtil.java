package com.studylog.api.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        // SecretKey 생성
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * AccessToken 생성
     */
    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenExpiration);
    }

    /**
     * RefreshToken 생성
     */
    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenExpiration);
    }

    /**
     * JWT 토큰 생성
     */
    private String generateToken(String email, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)  // 사용자 이메일
                .issuedAt(now)  // 발행 시간
                .expiration(expiryDate)  // 만료 시간
                .signWith(key)  // 서명
                .compact();
    }

    /**
     * JWT 토큰에서 이메일 추출
     */
    public String getEmailFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * JWT 토큰 검증
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * JWT 토큰 파싱
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
