package com.checkhouse.core.security.oauth2.service.naver;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.security.oauth2.service.OAuth2UserUnlink;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@Component
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink {

    @Value("${security.oauth2.client.registration.naver.client-id}")
    private String clientID;

    @Value("${security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    private static final String URL = "https://nid.naver.com/oauth2.0/token";
    private final RestTemplate restTemplate;


    @Override
    public void unlink(String accessToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String encodedState = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

        params.add("grant_type", "delete");
        params.add("client_id", clientID);
        params.add("client_secret", clientSecret);
        params.add("access_token", encodedState);
        params.add("service_provider", "NAVER");

        restTemplate.postForObject(URL, params, String.class);
    }
}
