package com.web.jaru.security.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRedisRepository extends CrudRepository<TokenRedis, Long> {
    Optional<TokenRedis> findByAccessToken(String accessToken);
    Optional<TokenRedis> findByRefreshToken(String refreshToken);

    void deleteByUserId(Long userId);
}
