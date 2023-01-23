package com.kyuwon.spring.global.config.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyuwon.spring.global.common.error.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 권한이 허가되지 않은 경우 호출
     *
     * @param request               AccessDeniedException 원인 요청
     * @param response              접근 실패 원인을 파악할 수 있는 응답
     * @param accessDeniedException 호출된 Exception
     */
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // 리디렉션할 오류페이지가 없기 때문에 403 코드 응답
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        objectMapper.writeValue(response.getWriter(), ErrorCode.ACCESS_DENIED);
    }

}