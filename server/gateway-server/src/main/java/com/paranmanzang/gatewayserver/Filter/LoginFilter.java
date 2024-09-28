package com.paranmanzang.gatewayserver.Filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paranmanzang.gatewayserver.jwt.CustomAuthenticationFailureHandler;
import com.paranmanzang.gatewayserver.jwt.CustomAuthenticationSuccessHandler;
import com.paranmanzang.gatewayserver.jwt.CustomReactiveAuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
                    log.info("Request body: {}", requestBody);

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        // Map<String, Object>로 받기
                        Map<String, Object> loginData = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {});
                        Object usernameObj = loginData.get("username");
                        Object passwordObj = loginData.get("password");

                        // username과 password가 객체인 경우 내부 값 확인
                        if (usernameObj instanceof Map) {
                            usernameObj = ((Map<?, ?>) usernameObj).get("value");
                        }
                        if (passwordObj instanceof Map) {
                            passwordObj = ((Map<?, ?>) passwordObj).get("value");
                        }

                        // username과 password가 String인지 확인
                        log.info("Received username object: {}", usernameObj);
                        log.info("Received password object: {}", passwordObj);

                        if (!(usernameObj instanceof String) || !(passwordObj instanceof String)) {
                            log.error("Invalid login attempt: Username or password is not a string");
                            return Mono.error(new BadCredentialsException("Invalid username or password"));
                        }

                        String username = (String) usernameObj;
                        String password = (String) passwordObj;

                        // username이나 password가 비어있는 경우 처리
                        if (username == null || password == null) {
                            log.error("Invalid login attempt: Missing username or password");
                            return Mono.error(new BadCredentialsException("Invalid username or password"));
                        }

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
                        log.info("Authentication token created for user: {}", authToken.getPrincipal());

                        // 인증 처리 시작
                        return authenticationManager.authenticate(authToken)
                                .flatMap(authentication -> {
                                    log.info("Authentication success for user: {}", authentication.getName());

                                    // SecurityContext에 인증 정보 설정
                                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                                    context.setAuthentication(authentication);
                                    SecurityContextHolder.setContext(context);

                                    return successHandler.handleSuccess(exchange, authentication)
                                            .then(Mono.defer(() -> {
                                                // 로그인 후 필터 체인으로 이동
                                                return webFilterChain.filter(exchange);
                                            }));
                                })
                                .onErrorResume(e -> {
                                    log.error("Authentication failed: {}", e.getMessage());
                                    return failureHandler.handleFailure(exchange, e);
                                });
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
