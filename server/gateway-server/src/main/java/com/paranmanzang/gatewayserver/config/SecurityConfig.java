package com.paranmanzang.gatewayserver.config;

import com.paranmanzang.gatewayserver.Filter.JwtGatewayFilter;
//import com.paranmanzang.gatewayserver.Filter.JwtWebFilter;
import com.paranmanzang.gatewayserver.Filter.ReissueFilter;
import com.paranmanzang.gatewayserver.jwt.*;
import com.paranmanzang.gatewayserver.Filter.LoginFilter;
import com.paranmanzang.gatewayserver.Filter.LogoutFilter;
import com.paranmanzang.gatewayserver.model.repository.UserRepository;
import com.paranmanzang.gatewayserver.oauth.CustomSuccessHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.*;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomReactiveAuthenticationManager customReactiveAuthenticationManager;
    private final JWTUtil jwtUtil;
    private final JwtTokenServiceImpl jwtTokenService;
    //private final JwtWebFilter jwtWebFilter;

    public SecurityConfig( //JwtWebFilter jwtWebFilter
                           JwtTokenServiceImpl jwtTokenService, JWTUtil jwtUtil, CustomAuthenticationSuccessHandler successHandler, CustomAuthenticationFailureHandler failureHandler, CustomReactiveAuthenticationManager customReactiveAuthenticationManager, CustomSuccessHandler customSuccessHandler, JwtTokenServiceImpl jwtTokenServiceImpl) {
        this.customSuccessHandler = customSuccessHandler;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.customReactiveAuthenticationManager = customReactiveAuthenticationManager;
        this.jwtUtil = jwtUtil;
        this.jwtTokenService = jwtTokenService;
        //this.jwtWebFilter = jwtWebFilter;

    }

    private void addTokenReissueFilter(ServerHttpSecurity http) {
        http.addFilterBefore(new ReissueFilter(jwtUtil, jwtTokenService), SecurityWebFiltersOrder.AUTHORIZATION);
    }
    @Bean
    public AuthenticationWebFilter loginFilter(){
        return new LoginFilter(customReactiveAuthenticationManager, successHandler, failureHandler);
    }

    @Bean
    public LogoutFilter logoutFilter() {
        return new LogoutFilter();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        configureCors(http);
        disableDefaultSecurity(http);
        addLoginFilter(http);
        //addJwtGatewayRouter(http);
        configureOAuth2(http);
        configureAuthorization(http);
        addLogoutFilter(http);
        addTokenReissueFilter(http);  // 토큰 재발급 필터 추가

        return http.build();
    }


    @Bean
    public JwtGatewayFilter jwtGatewayFilter() {
        return new JwtGatewayFilter(jwtUtil);
    }

/*    private void addJwtGatewayRouter(ServerHttpSecurity http) {
        log.info("Adding JWT Gateway Filter");
        http.addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHORIZATION);
    }*/

    private void configureCors(ServerHttpSecurity http) {
        http.cors(corsCustomizer -> corsCustomizer
                .configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    configuration.setMaxAge(3600L); // 1 hour
                    configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                    return configuration;
                }));
    }

    private void disableDefaultSecurity(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF 비활성화
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // 폼 로그인 비활성화
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable); // 기본 인증 비활성화
    }


    private void addLogoutFilter(ServerHttpSecurity http) {
       http.addFilterBefore(logoutFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
    }

    private void addLoginFilter(ServerHttpSecurity http) {
        log.info("Adding LoginFilter1");
        http.addFilterAt(loginFilter(), SecurityWebFiltersOrder.AUTHORIZATION);
        log.info("Adding LoginFilter2");
    }

    private void configureOAuth2(ServerHttpSecurity http) {
        http.oauth2Login(oauth2 -> oauth2
                .authenticationSuccessHandler(customSuccessHandler)
        );
    }

    private void configureAuthorization(ServerHttpSecurity http) {
        http.authorizeExchange(auth -> auth
                .pathMatchers("/**").permitAll()
                .pathMatchers("/login").permitAll() // /login 경로는 인증 없이 접근 가능
                .pathMatchers("/oauth2/**").permitAll() // OAuth2 경로는 인증 없이 접근 가능
                .anyExchange().authenticated() // 그 외의 모든 경로는 인증 필요
        );
    }

}