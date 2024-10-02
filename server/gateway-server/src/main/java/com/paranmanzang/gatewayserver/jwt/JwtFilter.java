/*
package com.paranmanzang.gatewayserver.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

    private final JWTUtil jwtUtil;


    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestUri = exchange.getRequest().getURI().getPath();

        if (requestUri.matches("^\\/login(?:\\/.*)?$") || requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
            return chain.filter(exchange); // 필터 체인을 통과시킴
        }

        return chain.filter(exchange)
                .flatMap((ex) -> {
                    String jwtToken = resolveToken(exchange);
                    if (jwtToken != null && !jwtUtil.isExpired(jwtToken)) {
                        String nickname = jwtUtil.getNickNameFromToken(jwtToken);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(nickname, null, null);

                        // ReactiveSecurityContextHolder에 인증 정보 설정
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                                .then(Mono.defer(() -> exchange.getResponse().setComplete()))
                                .doOnSuccess(unused -> {
                                    log.info("User '{}' has been authenticated and JWT tokens returned", nickname);
                                })
                                .doOnError(error -> {
                                    log.error("Failed to authenticate user '{}': {}", nickname, error.getMessage());
                                });
                    }

                    // JWT 토큰이 없거나 유효하지 않은 경우 필터 체인 계속 진행
                    return chain.filter(exchange);
                });

    }

    private String resolveToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7).trim(); // Bearer 이후의 토큰만 반환
    }
}
*/
