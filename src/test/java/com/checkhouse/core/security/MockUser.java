package com.checkhouse.core.security;

import com.checkhouse.core.entity.enums.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockUser {

    String name = "test@email.com";
    String role = Role.ROLE_USER.toString();
}
