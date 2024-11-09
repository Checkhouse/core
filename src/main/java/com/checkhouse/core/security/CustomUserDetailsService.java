package com.checkhouse.core.security;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.CustomUser;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.repository.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );

        log.info("[user {}]", user);

        CustomUser dto = new CustomUser(
                user.getEmail(),
                user.getRole().toString()
        );

        return new CustomUserDetails(dto);
    }
}
