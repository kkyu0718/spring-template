package com.kyuwon.spring.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyuwon.spring.domain.user.repository.UserRepository;
import com.kyuwon.spring.domain.user.service.UserFindService;
import com.kyuwon.spring.global.config.security.handler.JwtAccessDeniedHandler;
import com.kyuwon.spring.global.config.security.handler.JwtAuthenticationEntryPointHandler;
import com.kyuwon.spring.global.config.security.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security 설정 Config
 */
//TODO : spring security 설정 - access, refresh token
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] GET_PERMITTED_URLS = {
    };

    private static final String[] POST_PERMITTED_URLS = {
            "/api/auth/signup",
            "/api/auth/login"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/docs/**");
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPointHandler jwtAuthenticationEntryPointHandler,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) throws Exception {
        return httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                // form login filter 인 UsernamePasswordAuthenticationFilter 전에 auth filter를 사용
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 예외 처리
                .exceptionHandling(config -> config
                        // 인증되지 않은 사용자
                        .authenticationEntryPoint(jwtAuthenticationEntryPointHandler)
                        // 인증 o 권한 x
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                .authorizeHttpRequests(antz -> antz
                        .requestMatchers(HttpMethod.GET, GET_PERMITTED_URLS).permitAll()
                        .requestMatchers(HttpMethod.POST, POST_PERMITTED_URLS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/board/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); 암호화 방법 선택 가능
    }

    @Bean
    public AuthenticationManager authenticationManager(ApplicationContext context) throws Exception {
        AuthenticationManagerFactoryBean authenticationManagerFactoryBean = new AuthenticationManagerFactoryBean();
        authenticationManagerFactoryBean.setBeanFactory(context);
        return authenticationManagerFactoryBean.getObject();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    @Bean
    public JwtAuthenticationEntryPointHandler jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPointHandler(objectMapper);
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserFindService userFindService) {
        return new JwtAuthenticationFilter(jwtTokenProvider, userFindService);
    }

}