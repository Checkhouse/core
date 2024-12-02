package com.checkhouse.core.service;

import com.checkhouse.core.dto.Token;
import com.checkhouse.core.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public Token saveTokens(String userEmail) {
        String accessToken = jwtUtil.createAccessToken(userEmail);
        String refreshToken = jwtUtil.createRefreshToken(userEmail);

        redisTemplate.opsForValue().set("ACCESS_TOKEN:" + userEmail, accessToken);
        redisTemplate.opsForValue().set("REFRESH_TOKEN:" + userEmail, refreshToken);

        return new Token(accessToken, refreshToken);
    }

    public String getAccessTokensWithUserEmail(String userEmail) {
        return redisTemplate.opsForValue().get("ACCESS_TOKEN:" + userEmail);
    }
    public String getRefreshTokensWithUserEmail(String userEmail) {
        return redisTemplate.opsForValue().get("REFRESH_TOKEN:" + userEmail);
    }

    public void deleteTokensWithUserEmail(String userEmail) {
        redisTemplate.delete("ACCESS_TOKEN:" + userEmail);
        redisTemplate.delete("REFRESH_TOKEN:" + userEmail);
    }
    public void createExpireEvent() {
        // 10초 뒤에 사라지는 redis 생성
        // 네고 생성 후
        UUID negoId = UUID.randomUUID();
        String key = "NEGO:" + negoId.toString();
        String value = "NEGOEXPIRE";

        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.SECONDS);
    }
}
