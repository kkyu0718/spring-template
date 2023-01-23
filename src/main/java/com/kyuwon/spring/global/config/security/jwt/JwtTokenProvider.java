package com.kyuwon.spring.global.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final SecretKey secretKey;

    private final long accessTokenValidityInMillis;

    private final long refreshTokenValidityInMillis;

    private final UserDetailsService userDetailsService;

    private final JwtParser jwtParser;

    public JwtTokenProvider(
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
            @Value("${jwt.secret}") String secret,
            UserDetailsService userDetailsService) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenValidityInMillis = accessTokenValidity * 1000;
        this.refreshTokenValidityInMillis = refreshTokenValidity * 1000;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        this.userDetailsService = userDetailsService;
    }

    public String createAccessToken(Long userId) {
        return createToken(userId, ACCESS_TOKEN);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, REFRESH_TOKEN);
    }

    private String createToken(Long userId, String tokenType) {
        long expiredTimeMillis = tokenType.equals(ACCESS_TOKEN) ?
                accessTokenValidityInMillis : refreshTokenValidityInMillis;
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim(TOKEN_TYPE, tokenType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 및 만료기간 검사
    public boolean validateToken(String token) {
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        return !claimsJws.getBody().isEmpty();
    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String accessToken) {
        String usernameFromToken = jwtParser.parseClaimsJws(accessToken).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getSubject(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }
}