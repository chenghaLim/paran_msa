package com.paranmanzang.userservice.service.impl.jwt;

import com.paranmanzang.userservice.model.entity.User;
import com.paranmanzang.userservice.model.repository.UserRepository;
import com.paranmanzang.userservice.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class 
JwtTokenServiceImpl implements JwtTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final UserRepository userRepository;
    public JwtTokenServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void storeToken(String token, String userId, long duration) {
        //redis에 jwt를 저장, 만료 시간도 설정
        redisTemplate.opsForValue().set(token, userId, duration, TimeUnit.MILLISECONDS);
    }

    //로그아웃 시간 찍기
    public void updateLogoutTime(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setLogoutAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public String getUserFromToken(String token) {
        //redis에서 JWT 조회
        return redisTemplate.opsForValue().get(token);
    }
    public void blacklistToken(String token, long expirationTime){
        //블랙리스트에 토큰 추가 => 해당 토큰은 접속이 차단 됨
        redisTemplate.opsForValue().set(token, "blacklist",expirationTime, TimeUnit.MILLISECONDS);
    }
    public boolean isTokenBlacklisted(String token){
        //토큰이 블랙리스트에 있는지 확인
        String result = redisTemplate.opsForValue().get(token);
        return result != null&&result.equals("blacklist");
    }

    public void deleteToken(String token) {
        //Redis에서 JWT 삭제 (로그아웃 시 사용)
        redisTemplate.delete(token);
    }
    public boolean isExist(String token) {
        if(redisTemplate.hasKey(token)){
            return true;
        }
        else{return false;}
    }

}