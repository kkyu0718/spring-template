package com.kyuwon.spring.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyuwon.spring.global.common.error.ErrorResponse;
import com.kyuwon.spring.global.common.error.exception.BusinessException;
import com.kyuwon.spring.global.common.error.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT Access Token 이 없는 상태로 요청을 보내거나, 토큰 만료, 유효하지 않은 토큰 등을 이용했을 경우 호출
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    )
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");
        objectMapper.writeValue(response.getWriter(), errorCode);
    }
}