package com.kyuwon.spring.domain.user.dto.request;

public record SignUpRequest(
        String email,
        String password,
        String name,
        String country,
        String state,
        String city,
        String zipCode
        ) {
}
