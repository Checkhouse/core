package com.checkhouse.core.security.oauth2.service.naver;

import com.checkhouse.core.security.oauth2.service.OAuth2Provider;
import com.checkhouse.core.security.oauth2.service.OAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class NaverOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String nickName;
    private final String profileImageUrl;

    public NaverOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        log.info("attributes {}", attributes);

        this.accessToken = accessToken;

        Map<String, Object> naverProfile = (Map<String, Object>) attributes.get("response");
        this.attributes = naverProfile;

        log.info("[ NaverOAuth2UserInfo ] {}", this.attributes);

        this.id = naverProfile.get("id").toString();
        this.email = (String) naverProfile.get("email");

        this.name = (String) naverProfile.get("name");
        this.firstName = null;
        this.lastName = null;
        this.nickName = (String) naverProfile.get("nickname");

        this.profileImageUrl = (String)naverProfile.get("profile_image");

    }
    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getNickname() {
        return nickName;
    }

    @Override
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
