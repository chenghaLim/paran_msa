package com.paranmanzang.gatewayserver.Filter;

import com.paranmanzang.gatewayserver.Enum.Role;
import com.paranmanzang.gatewayserver.jwt.JWTUtil;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
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
            // 토큰이 없으면 필터 체인에 요청 전달
            if (jwtToken == null) {
                return chain.filter(exchange);
            }
            // 토큰 만료 여부 확인
            if (jwtUtil.isExpired(jwtToken)) {
                return sendErrorResponse(exchange, "Token expired", HttpStatus.UNAUTHORIZED);
            }

            String nickname = jwtUtil.getNickNameFromToken(jwtToken);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(nickname, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return chain.filter(exchange);
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