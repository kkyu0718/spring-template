package com.kyuwon.spring.domain.user.controller;

import com.kyuwon.spring.domain.user.domain.UserAccount;
import com.kyuwon.spring.domain.user.dto.request.LoginRequest;
import com.kyuwon.spring.domain.user.dto.request.SignUpRequest;
import com.kyuwon.spring.domain.user.dto.response.LoginResponse;
import com.kyuwon.spring.domain.user.dto.response.TokenResponse;
import com.kyuwon.spring.domain.user.service.LoginService;
import com.kyuwon.spring.domain.user.service.LogoutService;
import com.kyuwon.spring.domain.user.service.TokenService;
import com.kyuwon.spring.global.common.api.ApiResponse;
import com.kyuwon.spring.global.common.api.ResponseCode;
import com.kyuwon.spring.domain.user.dto.response.SignUpResponse;
import com.kyuwon.spring.domain.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final SignUpService signUpService;
    private final LoginService loginService;
    private final TokenService tokenService;
    private final LogoutService logoutService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Validated @RequestBody SignUpRequest request) {
        UserAccount user = signUpService.saveUser(
                request.name(),
                request.email(),
                request.password(),
                request.country(),
                request.state(),
                request.city(),
                request.zipCode()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(ResponseCode.USER_CREATED, SignUpResponse.of(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Validated @RequestBody LoginRequest request) {
        List<String> tokens = loginService.loginUser(
                request.email(),
                request.password()
        );

        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseCode.USER_LOGIN_SUCCESS, LoginResponse.of(accessToken, refreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        logoutService.logoutUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseCode.USER_LOGOUT_SUCCESS));
    }

    // refreshToken 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissueToken(
            @Validated @RequestHeader("Authorization") String refreshToken,
            Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<String> tokens = tokenService.refreshToken(refreshToken, userId);

        String newAccessToken = tokens.get(0);
        String newRefreshToken = tokens.get(1);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(ResponseCode.REFRESH_TOKEN, TokenResponse.of(newAccessToken, newRefreshToken)));
    }
}


