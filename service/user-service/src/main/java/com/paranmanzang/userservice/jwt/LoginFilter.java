package com.paranmanzang.userservice.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.paranmanzang.userservice.model.domain.user.CustomUserDetails;
import com.paranmanzang.userservice.model.domain.user.LoginModel;
import com.paranmanzang.userservice.model.repository.UserRepository;
import com.paranmanzang.userservice.service.impl.jwt.JwtTokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenServiceImpl jwtTokenService;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, JwtTokenServiceImpl jwtTokenService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/users/login/general");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginModel loginModel = new LoginModel();

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginModel = objectMapper.readValue(messageBody, LoginModel.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //클라이언트 요청에서 username, password 추출
        String username = loginModel.getUsername();
        String password = loginModel.getPassword();
        System.out.println(username);
        boolean state= userRepository.findByUsername(username).isState();
        System.out.println(state);
        if (!state) {
            throw new BadCredentialsException("UserModel account is inactive");
        }


        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }
        //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
                throws IOException, ServletException {
            // UserDetails 객체로부터 사용자 정보를 가져옴
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 사용자 정보를 가져옴
            String username = userDetails.getUsername();  // 사용자 ID 혹은 Username
            String nickname = "";  // 닉네임이 별도로 있는 경우 이를 설정

            if (userDetails instanceof CustomUserDetails) {
                // CustomUserDetails에서 닉네임을 가져오는 경우
                nickname = ((CustomUserDetails) userDetails).getNickname();
                System.out.println(nickname);
            }

            // 권한(Role)을 가져옴
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.iterator().next().getAuthority();

            // JWT 생성
            String access = jwtUtil.createJwt("access", username, role, nickname, 600000L);
            String refresh = jwtUtil.createJwt("refresh", username, role, nickname, 86400000L);

            // Refresh 토큰 Redis에 저장
            jwtTokenService.storeToken(refresh, username, 86400000L);

            // 응답에 JWT 추가
            response.setHeader("access", access);
            response.addCookie(createCookie("access",access));
            response.addCookie(createCookie("refresh", refresh));
            response.setStatus(HttpStatus.OK.value());
        }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
            //로그인 실패시 401 반환
            response.setStatus(401);
        }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
    }

