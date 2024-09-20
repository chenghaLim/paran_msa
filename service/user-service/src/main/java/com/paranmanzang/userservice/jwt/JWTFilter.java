package com.paranmanzang.userservice.jwt;


import com.paranmanzang.userservice.model.domain.user.CustomUserDetails;
import com.paranmanzang.userservice.model.domain.user.UserModel;
import com.paranmanzang.userservice.model.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.paranmanzang.userservice.model.domain.user.CustomOAuth2User;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키 또는 헤더에서 토큰을 확인
        String token = getTokenFromRequest(request);

        // 토큰이 없으면 다음 필터로 넘김
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "Token expired", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(token);
        if (!"access".equals(category)) {
            sendErrorResponse(response, "Invalid access token", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰에서 username, role 값을 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // 인증 토큰 생성 및 사용자 설정
        Authentication authToken = createAuthentication(username, role, request);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    // 쿠키 및 헤더에서 토큰을 추출하는 메소드
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = null;

        // 헤더에서 토큰 확인
        String accessToken = request.getHeader("access");
        if (accessToken != null) {
            token = accessToken;
        } else {
            // 쿠키에서 토큰 확인
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        return token;
    }

    // CustomUserDetails 또는 CustomOAuth2User를 생성하고 인증 객체를 생성
    private Authentication createAuthentication(String username, String role, HttpServletRequest request) {
        if (isOAuth2Request(request)) {
            // OAuth2 사용자
            UserModel userModel = new UserModel();
            userModel.setUsername(username);
            userModel.setRole(role);
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userModel);

            return new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        } else {
            // 일반 로그인 사용자
            User user = new User();
            user.setUsername(username);
            user.setRole(role);
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        }
    }

    // OAuth2 요청인지 확인하는 메소드 (간단한 예시)
    private boolean isOAuth2Request(HttpServletRequest request) {
        // 예시로 OAuth2 요청에 대한 URI 패턴을 확인
        return request.getRequestURI().contains("/oauth2");
    }

    // 에러 응답을 처리하는 메소드
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.print(message);
    }
}
