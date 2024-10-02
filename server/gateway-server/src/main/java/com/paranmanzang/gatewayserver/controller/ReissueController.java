package com.paranmanzang.gatewayserver.controller;
import com.paranmanzang.gatewayserver.Enum.Role;
import com.paranmanzang.gatewayserver.jwt.JWTUtil;
import com.paranmanzang.gatewayserver.jwt.JwtTokenServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;

@Controller
@Slf4j
@ResponseBody
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final JwtTokenServiceImpl jwtTokenService;

    public ReissueController(JWTUtil jwtUtil, JwtTokenServiceImpl jwtTokenService) {
        this.jwtUtil = jwtUtil;
        this.jwtTokenService = jwtTokenService;
    }
    public ResponseEntity<?> reissue(ServerWebExchange exchange) {
        log.info("reissue request received");

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String refreshToken = null; // refreshToken 변수를 초기화합니다.

        if (exchange.getRequest().getCookies().get("refresh").toString() != null) { // "refresh" 쿠키가 존재하는지 확인합니다.
            refreshToken = exchange.getRequest().getCookies().get("refresh").toString();
            refreshToken = refreshToken.substring(9, refreshToken.length() - 1);
            log.info("Refresh token 값 입력: {}", refreshToken);
        }
        String usernameFromR = jwtUtil.getUsernameFromToken(refreshToken);
        String userRoleFormR = jwtUtil.getRoleFromToken(refreshToken);
        String nicknameFromR = jwtUtil.getNickNameFromToken(refreshToken);

        log.info("usernameFromR : {}", usernameFromR);
        log.info("userRoleFormR : {}", userRoleFormR);
        log.info("nicknameFromR : {}", nicknameFromR);

        if(jwtTokenService.isExist(refreshToken)){
            String newAccessToken = jwtUtil.createAccessJwt(usernameFromR, userRoleFormR, nicknameFromR,600000L);
            String newRefreshToken = jwtUtil.createRefreshJwt(usernameFromR, userRoleFormR, nicknameFromR,86400000L);

            log.info("newAccessToken : {}", newAccessToken);
            log.info("newRefreshToken : {}", newRefreshToken);

            exchange.getResponse().getHeaders().remove(HttpHeaders.AUTHORIZATION);
            exchange.getResponse().getHeaders().add("Authorization", "Bearer " + newAccessToken);
            exchange.getResponse().addCookie(createCookie("refresh", refreshToken));
            exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE, "refresh=" + newRefreshToken + "; Path=/; HttpOnly");

            jwtTokenService.storeToken(newRefreshToken, nicknameFromR, 86400000L);

        }
        else{
            throw new RuntimeException("재로그인을 진행해주세요");
        }
        return ResponseEntity.ok("good");
    }
    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.fromClientResponse(key, value)
                .maxAge(86400)  // 쿠키의 만료 시간을 초 단위로 설정
                .path("/")
                .httpOnly(true)
                .build();
    }
}
