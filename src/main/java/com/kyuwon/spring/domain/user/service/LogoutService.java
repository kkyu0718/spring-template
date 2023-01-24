package com.kyuwon.spring.domain.user.service;

import com.kyuwon.spring.domain.user.domain.RefreshToken;
import com.kyuwon.spring.domain.user.domain.UserAccount;
import com.kyuwon.spring.domain.user.repository.TokenRedisRepository;
import com.kyuwon.spring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LogoutService {
    private final TokenService tokenService;

    @Transactional
    public void logoutUser(Long userId) {
        RefreshToken token = tokenService.findByUserId(userId);
        tokenService.deleteRefreshToken(token);
    }
}
