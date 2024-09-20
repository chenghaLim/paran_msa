package com.paranmanzang.userservice.oauth2;



import com.paranmanzang.userservice.jwt.JWTUtil;
import com.paranmanzang.userservice.model.domain.user.CustomOAuth2User;
import com.paranmanzang.userservice.service.impl.jwt.JwtTokenServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final JwtTokenServiceImpl jwtTokenServiceImpl;

    public CustomSuccessHandler(JWTUtil jwtUtil, JwtTokenServiceImpl jwtTokenServiceImpl) {
        this.jwtTokenServiceImpl = jwtTokenServiceImpl;
        this.jwtUtil = jwtUtil;
    }

    //소셜로그인
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String nickname = customUserDetails.getNickname();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt("access", username, role, nickname, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, nickname, 86400000L);


        response.setHeader("access", token);
        response.addCookie(createCookie("access", token));
        response.addCookie(createCookie("refresh", refreshToken));
        response.sendRedirect("http://localhost:3000/");
        jwtTokenServiceImpl.storeToken(refreshToken, nickname, 86400000L);
    }



    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
