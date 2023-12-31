package com.gunwook.jpeople.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate redisTemplate;

    // 리프레시 토큰 만료시간
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 24; // 24시간

    public void save(final RefreshToken refreshToken){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getUsername(),refreshToken.getRefreshToken());
        redisTemplate.expire(refreshToken.getUsername(), REFRESH_TOKEN_TIME, TimeUnit.SECONDS);
    }

    public Boolean delete(String username) {
        return redisTemplate.delete(username);
    }

    public Optional<RefreshToken> findByUsername(final String username) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String keyValue = String.valueOf(valueOperations.get(username));

        if (Objects.isNull(keyValue)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(username, keyValue));
    }



}
