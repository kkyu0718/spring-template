package com.kyuwon.spring.domain.user.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
