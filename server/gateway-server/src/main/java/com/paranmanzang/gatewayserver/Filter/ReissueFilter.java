package com.paranmanzang.gatewayserver.Filter;

import com.paranmanzang.gatewayserver.jwt.JWTUtil;
import com.paranmanzang.gatewayserver.jwt.JwtTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseCookie;

@Slf4j
@RequiredArgsConstructor
public class ReissueFilter implements WebFilter {
    private final JWTUtil jwtUtil;
    private final JwtTokenServiceImpl jwtTokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Refresh Token이 포함된 요청을 가로챕니다.
        String refreshToken = exchange.getRequest().getCookies().getFirst("refresh") != null
                ? exchange.getRequest().getCookies().getFirst("refresh").getValue()
                : null;

        if (refreshToken == null) {
            return chain.filter(exchange); // Refresh Token이 없으면 다음 필터로 진행
        }

        log.info("Reissue request received with refresh token: {}", refreshToken);

        // Refresh Token 검증 및 재발급 로직
        return reissue(exchange, refreshToken)
                .flatMap(responseEntity -> {
                    // 재발급된 응답을 생성
                    exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION, responseEntity.getBody().toString());
                    return chain.filter(exchange); // 다음 필터로 진행
                })
                .onErrorResume(e -> {
                    log.error("Error during token reissue: {}", e.getMessage());
                    // 예외 발생 시 500 상태 코드 반환
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Token reissue error: " + e.getMessage()).then();
                });
    }

    private Mono<ResponseEntity<?>> reissue(ServerWebExchange exchange, String refreshToken) {
        log.info("Reissue token logic started");

        // Refresh 토큰 파싱
        refreshToken = refreshToken.substring(9, refreshToken.length() - 1);

        // JWT 토큰 검증 및 정보 추출
        String usernameFromR = jwtUtil.getUsernameFromToken(refreshToken);
        String userRoleFromR = jwtUtil.getRoleFromToken(refreshToken);
        String nicknameFromR = jwtUtil.getNickNameFromToken(refreshToken);

        log.info("usernameFromR : {}", usernameFromR);
        log.info("userRoleFromR : {}", userRoleFromR);
        log.info("nicknameFromR : {}", nicknameFromR);

        // Refresh 토큰이 유효한지 검증
        return jwtTokenService.isExistM(refreshToken)
                .flatMap(isValid -> {
                    if (isValid) {
                        // 새로운 Access/Refresh 토큰 생성
                        String newAccessToken = jwtUtil.createAccessJwt(usernameFromR, userRoleFromR, nicknameFromR, 600000L);
                        String newRefreshToken = jwtUtil.createRefreshJwt(usernameFromR, userRoleFromR, nicknameFromR, 86400000L);

                        log.info("newAccessToken : {}", newAccessToken);
                        log.info("newRefreshToken : {}", newRefreshToken);

                        exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
                        exchange.getResponse().addCookie(createCookie("refresh", newRefreshToken));

                        // 새로운 Refresh 토큰 저장
                        return jwtTokenService.storeTokenM(newRefreshToken, nicknameFromR, 86400000L)
                                .then(Mono.just(ResponseEntity.ok("Token reissued successfully")));
                    } else {
                        return Mono.error(new RuntimeException("재로그인을 진행해주세요"));
                    }
                });
    }

    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .maxAge(86400)
                .path("/")
                .httpOnly(true)
                .build();
    }
}