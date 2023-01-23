package com.kyuwon.spring.global.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    // user
    USER_CREATED(HttpStatus.CREATED, "유저 회원가입에 성공하였습니다."),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "유저 로그인에 성공하였습니다."),
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "유저 로그아웃에 성공하였습니다."),

    // jwt
    REFRESH_TOKEN(HttpStatus.CREATED, "토큰 재발급에 성공하였습니다." );


    private final HttpStatus status;
    private final String message;

}