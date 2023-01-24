package com.kyuwon.spring.domain.user.dto.response;

public record TokenResponse(String accessToken, String refreshToken) {
    public static Object of(String newAccessToken, String newRefreshToken) {
        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
