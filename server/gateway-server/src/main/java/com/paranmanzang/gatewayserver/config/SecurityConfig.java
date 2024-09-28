package com.paranmanzang.gatewayserver.config;

import com.paranmanzang.gatewayserver.Filter.JwtGatewayFilter;
import com.paranmanzang.gatewayserver.jwt.CustomReactiveAuthenticationManager;
import com.paranmanzang.gatewayserver.Filter.LoginFilter;
import com.paranmanzang.gatewayserver.Filter.LogoutFilter;
import com.paranmanzang.gatewayserver.jwt.CustomAuthenticationFailureHandler;
import com.paranmanzang.gatewayserver.jwt.CustomAuthenticationSuccessHandler;
import com.paranmanzang.gatewayserver.jwt.JWTUtil;
import com.paranmanzang.gatewayserver.model.repository.UserRepository;
import com.paranmanzang.gatewayserver.oauth.CustomSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomReactiveAuthenticationManager customReactiveAuthenticationManager;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler, CustomAuthenticationFailureHandler failureHandler, CustomReactiveAuthenticationManager customReactiveAuthenticationManager, CustomSuccessHandler customSuccessHandler, UserRepository userRepository, JWTUtil jwtUtil, JwtGatewayFilter jwtGatewayFilter) {
        this.customSuccessHandler = customSuccessHandler;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.customReactiveAuthenticationManager = customReactiveAuthenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                // CORS 설정
                .cors(corsCustomizer -> corsCustomizer
                        .configurationSource(request -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            configuration.setAllowCredentials(true);
                            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "nickname"));
                            configuration.setMaxAge(3600L); // 1 hour
                            configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                            return configuration;
                        }))
                // 기본 보안 설정 비활성화
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // JWT 토큰 필터 추가
                .addFilterBefore((exchange, chain) -> {
                    return extractAndAuthenticateJWT(exchange)
                            .flatMap(authentication -> {
                                // 인증 정보가 있을 경우 SecurityContextHolder에 설정
                                if (authentication != null) {
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                }
                                return chain.filter(exchange);
                            });
                }, SecurityWebFiltersOrder.AUTHENTICATION)

                // 로그인 필터 추가
                .addFilterAt(new LoginFilter(customReactiveAuthenticationManager, successHandler, failureHandler), SecurityWebFiltersOrder.AUTHENTICATION)

                // 로그아웃 필터 추가
                .addFilterBefore(new LogoutFilter(), SecurityWebFiltersOrder.AUTHENTICATION)

                // OAuth2 설정
                .oauth2Login(oauth2 -> oauth2
                        .authenticationSuccessHandler(customSuccessHandler)
                )

                // 권한 설정
                .authorizeExchange(auth -> auth
                        .pathMatchers("/login", "/oauth2/**").permitAll()  // /login과 /oauth2/** 경로는 인증 없이 접근 가능
                        .pathMatchers("/api/groups/books").permitAll()
                        .anyExchange().authenticated() // 그 외 모든 경로는 인증 필요
                );


        return http.build();
    }

    private Mono<UsernamePasswordAuthenticationToken> extractAndAuthenticateJWT(ServerWebExchange exchange) {
        // 요청 헤더에서 Authorization 추출
        String token = resolveToken(exchange);
        if (token != null && jwtUtil.validateToken(token)) {
            // JWT 토큰에서 사용자 정보 추출
            String username = jwtUtil.getUsernameFromToken(token);
            // 권한이나 추가 정보는 필요에 따라 추가 가능
            return Mono.just(new UsernamePasswordAuthenticationToken(jwtUtil.getAuthorities(token), username, null));
        }
        return Mono.empty();
    }

    // Authorization 헤더에서 JWT 토큰을 추출하는 메서드
    private String resolveToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7).trim(); // Bearer 이후의 토큰만 반환
    }

}