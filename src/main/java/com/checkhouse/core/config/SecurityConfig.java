package com.checkhouse.core.config;

import com.checkhouse.core.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.checkhouse.core.security.CustomUserDetailsService;
import com.checkhouse.core.security.JwtFilter;
import com.checkhouse.core.security.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.checkhouse.core.security.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.checkhouse.core.security.oauth2.service.CustomOAuth2UserService;
import com.checkhouse.core.service.RedisService;
import com.checkhouse.core.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    @Value("${cors.allowed-origins.${spring.profiles.active}}")
    private List<String> allowOriginList;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.headers(
                headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.sessionManagement(
                sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(
                new JwtFilter(customUserDetailsService, jwtUtil, redisService),
                UsernamePasswordAuthenticationFilter.class
        );

        http.authorizeHttpRequests(
                (authorize) ->
                        authorize
                                .requestMatchers(antMatcher("/login/**" )).permitAll()         // oauth
                                .requestMatchers(antMatcher("swagger-ui/**")).permitAll()      // api-docs
                                .requestMatchers(antMatcher("/swagger-ui.html")).permitAll()
                                .requestMatchers(antMatcher("swagger-resources/**")).permitAll()
                                .requestMatchers(antMatcher("/api-docs")).permitAll()
                                .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                                .anyRequest().authenticated()
        );

        http.oauth2Login(configure ->
                configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                        .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
        );

        return http.build();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(allowOriginList); // 허용할 Origin 추가
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/api-docs",
                        "swagger-ui/**",
                        "/swagger-ui.html",
                        "swagger-resources/**",
                        "/v3/api-docs/**"
                );
    }
}
