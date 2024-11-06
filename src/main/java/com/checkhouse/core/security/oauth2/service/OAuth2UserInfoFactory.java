package com.checkhouse.core.security.oauth2.service;

import com.checkhouse.core.security.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.checkhouse.core.security.oauth2.service.google.GoogleOAuth2UserInfo;
import com.checkhouse.core.security.oauth2.service.kakao.KakaoOAuth2UserInfo;
import com.checkhouse.core.security.oauth2.service.naver.NaverOAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
                                                   String accessToken,
                                                   Map<String, Object> attributes) {
        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
            log.info("[ Provider ]: Google ");
            return new GoogleOAuth2UserInfo(accessToken, attributes);
        } else if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            log.info("[ Provider ]: Kakao ");
            return new KakaoOAuth2UserInfo(accessToken, attributes);
        } else if(OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) {
            log.info("[ Provider ]: Naver ");
            return new NaverOAuth2UserInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}
