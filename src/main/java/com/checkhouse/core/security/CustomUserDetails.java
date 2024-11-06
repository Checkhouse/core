package com.checkhouse.core.security;

import com.checkhouse.core.dto.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails  {

    private CustomUser user;

    public CustomUserDetails(CustomUser user) {
        this.user = user;
    }
    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        log.info("[ CustomUserDetails ] user {}", user.name() );
        // todo
        List<String> roles = Arrays.asList(new String[]{user.role()});

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.name();
    }

}
