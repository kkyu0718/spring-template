package com.kyuwon.spring.global.common.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    ACCESS_DENIED(403, "C006", "Access is Denied"),


    // Auth
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

    // User
    USER_NOT_FOUND(404, "U001", "User not found"),

    // Jwt
    TOKEN_USER_NOT_FOUND(400, "J001", "토큰의 유저가 존재하지 않습니다."),
    TOKEN_NOT_FOUND(400, "J002", "토큰이 존재하지 않습니다."),
    INVALID_JWT_SIGNATURE(400, "J003", "유효하지 않은 jwt 서명입니다."),
    TOKEN_EXPIRED(400, "J004", "토큰이 만료되었습니다."),
    UNSUPPORTED_JWT(400, "J005", "지원하지 않는 토큰 형식입니다."),
    ILLEGAL_ARGUMENT(400, "J006", "토큰의 compact가 invalid합니다."),
    REFRESH_TOKEN_EXPIRED(400, "J007", "refresh 토큰이 만료되었습니다. 다시 로그인해주세요."),
    NO_DB_TOKEN(400, "J008", "db에 refresh token이 없습니다. 로그인하지 않은 유저입니다." ),
    USER_LOGOUTED(400,"J009" , "토큰의 유저는 로그아웃 되었습니다.");
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


}