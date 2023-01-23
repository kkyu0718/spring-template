package com.kyuwon.spring.domain.user.service;

import com.kyuwon.spring.domain.user.domain.UserAccount;
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
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    @Transactional
    public void logoutUser(Long userId) {
        UserAccount user = userFindService.findById(userId);
        user.setRefreshToken(null);
        userRepository.save(user);
    }
}
