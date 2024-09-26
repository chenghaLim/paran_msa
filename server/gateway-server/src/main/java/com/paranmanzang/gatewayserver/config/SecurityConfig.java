package com.paranmanzang.gatewayserver.config;

import com.paranmanzang.gatewayserver.jwt.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // CSRF 비활성화
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)  // Form-based 로그인 비활성화
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)  // HTTP Basic 인증 비활성화
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/swagger-ui/**").permitAll()  // Swagger UI 경로 허용
                        .pathMatchers("/get").permitAll()  // 특정 경로 허용
                        .pathMatchers("/api/comments/{test}").permitAll()  // 매개변수 포함 경로 허용
                        .pathMatchers("/api/groups/{test}").permitAll()  // 매개변수 포함 경로 허용
                        .pathMatchers("/api/users/create").permitAll()  // 사용자 생성 경로 허용
                        .pathMatchers("/api/**").permitAll()  // 모든 API 경로 허용
                        .anyExchange().authenticated()  // 나머지 경로는 인증 필요
                )
                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)  // JWT 필터 추가
                .build();
    }


    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOriginPattern("http://localhost:3000");  // 허용할 출처 패턴
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // 허용할 메서드
        corsConfig.addAllowedHeader("*");  // 모든 헤더 허용
        corsConfig.setAllowCredentials(true);  // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}


