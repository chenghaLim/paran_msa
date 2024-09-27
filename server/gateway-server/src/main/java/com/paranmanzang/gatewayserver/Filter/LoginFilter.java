package com.paranmanzang.gatewayserver.Filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paranmanzang.gatewayserver.jwt.CustomAuthenticationFailureHandler;
import com.paranmanzang.gatewayserver.jwt.CustomAuthenticationSuccessHandler;
import com.paranmanzang.gatewayserver.jwt.CustomReactiveAuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class LoginFilter extends AuthenticationWebFilter {
    private final CustomReactiveAuthenticationManager authenticationManager; // authenticationManager 필드 추가
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    public LoginFilter(CustomReactiveAuthenticationManager authenticationManager, CustomAuthenticationSuccessHandler successHandler, CustomAuthenticationFailureHandler failureHandler) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager; // 필드 초기화
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;

    }

    private Mono<Void> convert(ServerWebExchange exchange, WebFilterChain webFilterChain) {

        return exchange.getRequest().getBody()
                .next()
                .flatMap(dataBuffer -> {
                    String requestBody = dataBuffer.toString(StandardCharsets.UTF_8);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, String> loginData = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>() {});
                        String username = loginData.get("username");
                        String password = loginData.get("password");

                        if (username == null || password == null) {
                            return Mono.error(new BadCredentialsException("Invalid username or password"));
                        }
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
                        log.info("Authenticated user: {}", username);
                        log.info("Authenticated password: {}", password);
                        log.info("Authenticated token: {}", authToken.getPrincipal());
                        return authenticationManager.authenticate(authToken)
                                .doOnSuccess(authentication ->
                                        successHandler.handleSuccess(exchange, authentication)
                                ) // 수정된 부분
                                .doOnError(e -> failureHandler.handleFailure(exchange, e)); // 수정된 부분
                    } catch (IOException e) {
                        return Mono.error(new IllegalArgumentException("Invalid username or password"));
                    }
                })
                .then(); // Mono<Void> 반환
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().equals("/login")) {
            log.info("Authenticated user 여기 로그인 필터 핉터: {}", exchange.getPrincipal());
            return convert(exchange, chain);
        }
        // 로그인 요청이 아닐 경우 다음 필터로 진행
        return chain.filter(exchange);
    }

}
