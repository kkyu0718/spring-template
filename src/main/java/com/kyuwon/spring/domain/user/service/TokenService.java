package com.kyuwon.spring.domain.user.service;

import com.kyuwon.spring.domain.user.domain.UserAccount;
import com.kyuwon.spring.domain.user.dto.response.TokenResponse;
import com.kyuwon.spring.domain.user.repository.UserRepository;
import com.kyuwon.spring.global.common.error.exception.BusinessException;
import com.kyuwon.spring.global.common.error.exception.ErrorCode;
import com.kyuwon.spring.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserFindService userFindService;

    public TokenResponse refreshToken(String refreshToken, Long userId) {
        UserAccount userAccount = userFindService.findById(userId);
        String dbToken = userAccount.getRefreshToken();

        checkRefreshToken(dbToken, refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

        // db의 refresh token 갱신
        saveRefreshToken(userAccount, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    private void checkRefreshToken(String dbToken, String refreshToken) {
        if(dbToken == null) {
            throw new BusinessException(ErrorCode.NO_DB_TOKEN);
        }

        if(!dbToken.equals(refreshToken.substring(JwtTokenProvider.TOKEN_PREFIX.length()))) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }

    @Transactional
    public void saveRefreshToken(UserAccount user, String refreshToken) {
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
