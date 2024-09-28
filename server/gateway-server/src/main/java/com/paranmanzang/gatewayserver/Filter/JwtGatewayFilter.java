package com.paranmanzang.gatewayserver.Filter;

import com.paranmanzang.gatewayserver.Enum.Role;
import com.paranmanzang.gatewayserver.jwt.JWTUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@Component
public class JwtGatewayFilter extends AbstractGatewayFilterFactory<JwtGatewayFilter.Config> {

    private final JWTUtil jwtUtil;

    public JwtGatewayFilter(JWTUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String jwtToken = resolveToken(exchange);
            log.info("Resolved JWT Token: {}", jwtToken);

            if (jwtToken == null) {
                log.warn("JWT token is missing");
                return chain.filter(exchange);
            }

            if (jwtUtil.isExpired(jwtToken)) {
                log.warn("JWT token has expired");
                return sendErrorResponse(exchange, "Token expired", HttpStatus.UNAUTHORIZED);
            }

            try {
                String nickname = jwtUtil.getNickNameFromToken(jwtToken);
                log.info("Extracted username from JWT: {}", nickname);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(nickname, null, null);

                // ReactiveSecurityContextHolder에 저장
                return Mono.deferContextual(Mono::just)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                        .then(chain.filter(exchange)) // 필터 체인 계속 진행
                        .doOnSuccess(unused -> log.info("Authenticated user '{}' with JWT token", nickname));

            } catch (Exception e) {
                log.error("JWT token validation failed: {}", e.getMessage());
                return sendErrorResponse(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    // 쿠키에서 토큰 추출
    private String resolveToken(ServerWebExchange exchange) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String accessToken = authHeader.substring(7); // "Bearer " 이후의 토큰만 추출

        String refreshToken = exchange.getRequest().getCookies().getFirst("refresh") != null ?
                exchange.getRequest().getCookies().getFirst("refresh").getValue() : null;
        if (refreshToken == null) {
            return null;
        }
        // 두 토큰의 닉네임이 같지 않으면 인증 실패
        if (!jwtUtil.getNickNameFromToken(accessToken).equals(jwtUtil.getNickNameFromToken(refreshToken))) {
            throw new IllegalArgumentException("인증 실패");
        }
        return accessToken;
    }

    // 에러 응답 처리
    private Mono<Void> sendErrorResponse(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Data
    public static class Config {
        private List<Role> roles;
    }
}