package com.team15.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    // 1. 진짜 JWT 토큰을 만들어주는 메서드 (기존 지희님 코드)
    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. [추가] 프론트엔드가 요청 헤더에 보낸 Bearer 토큰 쏙 뽑아내기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 뒷부분의 실제 토큰만 잘라내기
        }
        return null;
    }

    // 3. [추가] 토큰 복호화해서 유저 이메일 알아내기
    public String getUserEmail(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // 토큰 만들 때 넣었던 email(Subject) 리턴
    }
}