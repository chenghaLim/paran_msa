package com.paranmanzang.gatewayserver.jwt;

import com.paranmanzang.gatewayserver.model.Domain.oauth.CustomUserDetails;
import com.paranmanzang.gatewayserver.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final JwtTokenServiceImpl jwtTokenService;

    public CustomAuthenticationSuccessHandler(JWTUtil jwtUtil, JwtTokenServiceImpl jwtTokenService) {
        this.jwtUtil = jwtUtil;
        this.jwtTokenService = jwtTokenService;
    }

    public Mono<Void> handleSuccess(ServerWebExchange exchange, Authentication authentication) {
        log.info("authentication : {}", authentication.getPrincipal());
        User user = (User) authentication.getPrincipal();
        CustomUserDetails userDetails = new CustomUserDetails(user); // User를 기반으로 CustomUserDetails 생성        log.info("userDetails: {}", userDetails);
        log.info("userDetails : {}", userDetails);

        String username = userDetails.getUsername();
        String nickname = userDetails.getNickname();
        log.info("username : {}", username);
        log.info("nickname : {}", nickname);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.iterator().next().getAuthority();
        log.info("role : {}", role);

        String access = jwtUtil.createAccessJwt(username, role, nickname, 600000L);
        log.info("access : {}", access);

        String refresh = jwtUtil.createRefreshJwt(username, role, nickname, 86400000L);
        log.info("refresh : {}", refresh);

        jwtTokenService.storeToken(refresh, username, 86400000L);

        exchange.getResponse().getHeaders().add("Authorization", "Bearer " + access);
        exchange.getResponse().addCookie(createCookie("refresh", refresh));
        exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE, "refresh=" + refresh + "; Path=/; HttpOnly");
        exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE, "token=; Path=/; HttpOnly; Max-Age=0");
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        log.info("Response Cookies: {}", exchange.getResponse().getCookies());


        return exchange.getResponse().setComplete();
    }


    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .maxAge(86400)
                .path("/")
                .httpOnly(true)
                .secure(true)  // HTTPS에서만 전송
                .sameSite("None")  // Cross-site 요청 허용
                .build();
    }
    }

