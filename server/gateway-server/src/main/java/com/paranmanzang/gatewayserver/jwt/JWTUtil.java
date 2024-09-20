package com.paranmanzang.gatewayserver.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    // secret 값을 받아서 SecretKey로 변환
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 토큰 만료 여부 확인
    public Boolean isExpired(String token) {
        // 토큰에서 만료일을 추출하여 현재 시간과 비교
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().before(new Date());
    }

    // 토큰에서 클레임 추출 (서명 검증 포함)
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 서명 검증을 위한 비밀 키 설정
                .build()
                .parseSignedClaims(token)
                .getPayload(); // 서명이 검증된 후 클레임 반환
    }
}
