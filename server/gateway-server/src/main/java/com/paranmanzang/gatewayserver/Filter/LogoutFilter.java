package com.paranmanzang.gatewayserver.Filter;

import com.paranmanzang.gatewayserver.jwt.JwtTokenServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LogoutFilter implements WebFilter {

    @Autowired
    private JwtTokenServiceImpl jwtTokenService; // JWT 토큰 서비스를 주입받습니다.

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 로그아웃 요청인지 확인
        if (exchange.getRequest().getURI().getPath().equals("/logout")) {
            log.info("Logout request received");
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String refreshToken = null; // refreshToken 변수를 초기화합니다.
            log.info("Refresh token header: {}", authHeader);
            if (exchange.getResponse().getCookies().containsKey("refresh")) { // "refresh" 쿠키가 존재하는지 확인합니다.
                refreshToken = exchange.getResponse().getCookies().getFirst("refresh").getValue(); // 쿠키 값을 가져옵니다.
            }
            log.info("Refresh token: {}", refreshToken);
            if(refreshToken != null) {
                jwtTokenService.deleteToken(refreshToken);
                exchange.getResponse().addCookie(createCookie("refresh", null));
            }
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출
                log.info("token : {}", token);
                jwtTokenService.blacklistToken(token, 600000);
                // 응답에서 Authorization 헤더를 삭제합니다.
                exchange.getResponse().getHeaders().remove(HttpHeaders.AUTHORIZATION);
                // 추가적인 로그아웃 처리를 할 수 있습니다.
                exchange.getResponse().setStatusCode(HttpStatus.OK);

                return exchange.getResponse().setComplete(); // 응답 완료
            }
        }
        return chain.filter(exchange); // 필터 체인을 계속 진행
    }

    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.fromClientResponse(key, value)
                .maxAge(0)  // 쿠키의 만료 시간을 초 단위로 설정
                .path("/")
                .httpOnly(true)
                .build();
    }
}
