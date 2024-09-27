package com.paranmanzang.gatewayserver.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final SecretKey refreshSecretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, @Value("${spring.jwt.refreshSecret}") String refreshSecret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshSecretKey = new SecretKeySpec(refreshSecret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());

    }
    //access토큰 발급(get용) claim없는 토큰
    //public Mono<Claims> parseToken(String token) {}

    //access토큰 발급(Post용)
    public String createAccessJwt(String username, String role, String nickname, Long expiredMs) {
        return Jwts.builder()
                .claim("category", "access")
                .claim("username", username)
                .claim("nickname", nickname)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    //refresh토큰 발급(Post용)
    public String createRefreshJwt(String username, String role, String nickname, Long expiredMs) {
        return Jwts.builder()
                .claim("category", "refresh")
                .claim("username", username)
                .claim("nickname", nickname)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(refreshSecretKey)
                .compact();
    }

    // 토큰의 만료 여부를 확인
    public Boolean isExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().before(new Date());
    }

    // 특정 클레임의 값을 반환
    public String getClaim(String token, String claimName) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(claimName, String.class);
    }

    // 토큰에서 모든 클레임을 추출
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰에서 닉네임 클레임을 추출
    public String getNickNameFromToken(String token) {
        return getClaim(token, "nickname");
    }

    public List<String> getRoleFromToken(String token) {
        String role = getClaim(token, "role");
        return List.of(role.split(",")); // 역할을 콤마로 구분된 문자열로 가정
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, "username");
    }
    // 토큰에서 카테고리 클레임을 추출
    public String getCategoryFromToken(String token) {
        return getClaim(token, "category");
    }
}


