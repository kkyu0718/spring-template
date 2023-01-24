package com.kyuwon.spring.domain.user.service;

import com.kyuwon.spring.domain.user.domain.RefreshToken;
import com.kyuwon.spring.domain.user.domain.UserAccount;
import com.kyuwon.spring.domain.user.repository.TokenRedisRepository;
import com.kyuwon.spring.domain.user.repository.UserRepository;
import com.kyuwon.spring.global.common.error.exception.BusinessException;
import com.kyuwon.spring.global.common.error.exception.ErrorCode;
import com.kyuwon.spring.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRedisRepository tokenRedisRepository;
    private final RedisTemplate redisTemplate;

    @Transactional
    public List<String> refreshToken(String refreshToken, Long userId) {
        RefreshToken dbData = findByToken(refreshToken);
        String dbToken = dbData.getRefreshToken();

        checkRefreshToken(dbToken, refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

        // db의 refresh token 갱신
        dbData.setRefreshToken(newRefreshToken);
        tokenRedisRepository.save(dbData);

        return List.of(newAccessToken, newRefreshToken);
    }

    private void checkRefreshToken(String dbToken, String refreshToken) {
        if(dbToken == null) {
            throw new BusinessException(ErrorCode.NO_DB_TOKEN);
        }

        if(dbToken.equals(refreshToken) == false) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }
    }

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .userId(userId)
                .build();

        tokenRedisRepository.save(token);
    }

    public RefreshToken findByToken(String refreshToken) {
        RefreshToken token = tokenRedisRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_DB_TOKEN));
        return token;
    }

    public RefreshToken findByUserId(Long userId) {
        RefreshToken token = tokenRedisRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_DB_TOKEN));
        return token;
    }

    @Transactional
    public void deleteRefreshToken(RefreshToken token) {
        tokenRedisRepository.delete(token);
        return;
    }
}
