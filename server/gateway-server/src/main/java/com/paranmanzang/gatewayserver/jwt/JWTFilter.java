package com.paranmanzang.gatewayserver.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JWTFilter implements WebFilter {

    private final JWTUtil jwtUtil; // JWT 유틸리티 클래스

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = getTokenFromRequest(exchange);

        if (token == null) {
            return chain.filter(exchange);
        }

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            return sendErrorResponse(exchange, "Token expired", HttpStatus.UNAUTHORIZED);
        }

        // 계속 필터 체인으로 전달
        return chain.filter(exchange);
    }

    // 헤더 또는 쿠키에서 JWT 토큰 추출
    private String getTokenFromRequest(ServerWebExchange exchange) {
        // 헤더에서 토큰 확인
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // "Bearer " 부분을 제거
        }

        // 쿠키에서 토큰 확인
        HttpCookie cookie = exchange.getRequest().getCookies().getFirst("Authorization");
        if (cookie != null) {
            return cookie.getValue();  // 쿠키의 값을 반환
        }

        return null; // 토큰이 없는 경우 null 반환
    }

    // 에러 응답 처리
    private Mono<Void> sendErrorResponse(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}
