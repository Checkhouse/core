package com.checkhouse.core.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 *
 * jwt 생성  추춣을 위한 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long expireTime;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTime;
    private SecretKey key;
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String createAccessToken(String email) {
        return createToken(email, expireTime);
    }
    public String createRefreshToken(String email) {
        return createToken(email, refreshTime);

    }
    /**
     * TODO 유효한 RFT 로 ACT 재발급 시 RFT 재발행 로직 추가
     */
    public String reIssueAccessToken(String oldAccessToken) {

        Claims claims = parseClaims(oldAccessToken);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(tokenValidity.toInstant()))
                .signWith(key)
                .compact();
    }
    /**
     * JWT 생성
     * @param issuer
     * @param expireTime
     * @return JWT String
     */
    private String createToken(String issuer, Long expireTime) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .issuer(issuer)
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(tokenValidity.toInstant()))
                .signWith(key)
                .compact();
    }

    /**
     * Token에서 User Id 추출
     * @param token
     * @return User Id
     */
    public String getUserEmail (String token) {
        return parseClaims(token).getIssuer();
    }

    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public Boolean validateToken(String token) {
        try {
            log.debug("valid Token");

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token).getPayload();

            return true;
        } catch (SecurityException e) {
            log.info("Invalid JWT Token", e);
            return false;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            return false;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            return false;
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            return false;
        }
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
