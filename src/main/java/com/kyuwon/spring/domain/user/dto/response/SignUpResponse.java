package com.kyuwon.spring.domain.user.dto.response;

import com.kyuwon.spring.domain.user.domain.UserAccount;
import com.kyuwon.spring.domain.user.model.Address;

public record SignUpResponse(
        String email,
        String password,
        String name,
        Address address
) {
    public static SignUpResponse of (UserAccount entity) {
        return new SignUpResponse(
                entity.getEmail(),
                entity.getPassword(),
                entity.getName(),
                entity.getAddress()
        );
    }
}
