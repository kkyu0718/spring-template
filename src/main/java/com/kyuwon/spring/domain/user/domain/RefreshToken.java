package com.kyuwon.spring.domain.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash(value = "user", timeToLive = 180) // 3min
public class RefreshToken {

    @Id
    private Long userId;

    @Indexed
    private String refreshToken;

    @Builder
    public RefreshToken(Long userId, String refreshToken){
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

}