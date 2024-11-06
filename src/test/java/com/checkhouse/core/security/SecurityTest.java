package com.checkhouse.core.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



@WithMockUser
public class SecurityTest {

    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void 시큐리티_테스트1() {
        System.out.println("test1: " + SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void 시큐리티_테스트2() {
        System.out.println("test2: " + SecurityContextHolder.getContext().getAuthentication());
    }

}
