package com.paranmanzang.userservice.controller;

import com.paranmanzang.userservice.jwt.JWTUtil;
import com.paranmanzang.userservice.service.impl.jwt.JwtTokenServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final JwtTokenServiceImpl jwtTokenServiceImpl;

    public ReissueController(JWTUtil jwtUtil, JwtTokenServiceImpl jwtTokenServiceImpl) {
        this.jwtUtil = jwtUtil;
        this.jwtTokenServiceImpl = jwtTokenServiceImpl;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // Get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }
        if (refresh == null) {
            return new ResponseEntity<>("Refresh token is null", HttpStatus.BAD_REQUEST);
        }

        // Check if token is expired
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Refresh token is expired", HttpStatus.BAD_REQUEST);
        }

        // Check if token category is "refresh"
        String category = jwtUtil.getCategory(refresh);
        if (!"refresh".equals(category)) {
            return new ResponseEntity<>("Invalid token category", HttpStatus.BAD_REQUEST);
        }

        // Check if the refresh token exists in the database
        Boolean isExist = jwtTokenServiceImpl.isExist(refresh);
        if (!isExist) {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String nickname = jwtUtil.getNickName(refresh);

        if (username == null || role == null || nickname == null) {
            return new ResponseEntity<>("Invalid token payload", HttpStatus.BAD_REQUEST);
        }

        // Create new JWT tokens
        String newAccess = jwtUtil.createJwt("access", username, role, nickname, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, nickname, 86400000L);

        // Delete old refresh token and store new refresh token
        jwtTokenServiceImpl.deleteToken(refresh);
        jwtTokenServiceImpl.storeToken(newRefresh, username, 86400000L);

        // Delete old refresh cookie
        Cookie deleteCookie = new Cookie("refresh", null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        response.addCookie(deleteCookie);

        // Add new refresh cookie
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // Enable this if using HTTPS
        return cookie;
    }
}