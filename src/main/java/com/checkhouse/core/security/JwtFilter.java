package com.checkhouse.core.security;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.service.RedisService;
import com.checkhouse.core.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("[ JwtFilter ] doFilterInternal");

        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("Authorization-refreshToken");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7); // "Bearer " 이후의 토큰 부분만 추출
        }

        // JWT is present in the header
        if (accessToken != null ) {
            // Validate the JWT token
            try {
                // 토큰 검증
                if (jwtUtil.validateToken(accessToken)) {
                    String email = jwtUtil.getUserEmail(accessToken);

                    String redisAccessToken = redisService.getAccessTokensWithUserEmail(email);

                    log.info("[ JwtFilter: validate access token ]"); // access token 확인
                    if(!accessToken.equals(redisAccessToken)) throw new GeneralException(ErrorStatus._INVALID_ACCESS_TOKEN);
                    log.info("[ email in JwtFilter ] {}", email);

                    // Create userDetails if the user and token match
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                    if (userDetails != null) {

                        log.info("[ userDetail ] {}", email);
                        // Create an authentication token with UserDetails, Password, and Role
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        // Set the authentication in the current request's Security Context
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        log.info("[ authentication Success ] {}", email);
                    }
                }
            } catch (ExpiredJwtException e) {
                // todo 재발급 과정
                // 엑세스 토큰이 만료된 경우, 해당 엑세스 토큰의 리프레시 토큰을 조회
                // 리프레시 토큰이 살아있을 경우, 엑세스 토큰과 리프레시 토큰 다시 발급

                // 리프레시 토큰이 만료된 경우, 해당 엑세스 토큰과 리프레시 토큰 모두 삭제 후 재로그인 진행 요청
                String userEmail = jwtUtil.getUserEmail(accessToken);

                // validate rft
                if (jwtUtil.validateToken(refreshToken)) {
                    // Reissue act
                    String newACT = jwtUtil.reIssueAccessToken(accessToken);
                    String userKakaoId = jwtUtil.getUserEmail(newACT);

                    // Create userDetails if the user and token match
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userKakaoId);

                    if (userDetails != null) {
                        // Create an authentication token with UserDetails, Password, and Role
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                        response.setHeader("Authorization", newACT);
                        // Set the authentication in the current request's Security Context
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
                log.warn("filter exception");
            }
        }

        filterChain.doFilter(request, response); // Pass the request to the next filter
    }
}
