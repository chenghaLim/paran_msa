package com.paranmanzang.gatewayserver.service;


public interface JwtTokenService {
    void storeToken(String token, String userId, long duration);
    String getUserFromToken(String token);
    void blacklistToken(String token, long expirationTime);
    boolean isTokenBlacklisted(String token);
    void deleteToken(String token);
}
