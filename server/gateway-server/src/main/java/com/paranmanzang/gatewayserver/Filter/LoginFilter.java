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
        log.info("Request received at: {}", exchange.getRequest().getPath()); // 요청 경로 로그

        return exchange.getRequest().getBody()
                .next()
                .flatMap(dataBuffer -> {
                    // Body 읽기 시작
                    log.info("Reading request body...");
                    String requestBody = dataBuffer.toString(StandardCharsets.UTF_8);
                    log.info("Request body: {}", requestBody); // 요청 본문 로그

                    try {
                        // JSON 파싱 시작
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, String> loginData = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>() {});
                        String username = loginData.get("username");
                        String password = loginData.get("password");

                        // username과 password 로그
                        log.info("Parsed username: {}", username);
                        log.info("Parsed password: {}", password);

                        // username 또는 password가 없을 경우 에러
                        if (username == null || password == null) {
                            log.error("Invalid login attempt: Missing username or password");
                            return Mono.error(new BadCredentialsException("Invalid username or password"));
                        }

                        // 인증 토큰 생성
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
                        log.info("Authentication token created for user: {}", authToken.getPrincipal());

                        // 인증 처리 시작
                        return authenticationManager.authenticate(authToken)
                                .doOnSuccess(authentication -> {
                                    log.info("Authentication success for user: {}", authentication.getName());
                                    successHandler.handleSuccess(exchange, authentication);
                                }) // 성공 시 처리
                                .doOnError(e -> {
                                    log.error("Authentication failed: {}", e.getMessage());
                                    failureHandler.handleFailure(exchange, e);
                                }); // 실패 시 처리
                    } catch (IOException e) {
                        log.error("Error parsing request body: {}", e.getMessage());
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
