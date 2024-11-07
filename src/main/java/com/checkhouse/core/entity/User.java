package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="user_id")
    private UUID userId;

    @Column(name="user_name")
    private String username;

    @Column(name="email")
    private String email;

    @Column(name="nickname", nullable=true)
    private String nickname = null;

    @Column(name="password", nullable=true)
    private String password = null;

    @Column(name="provider", nullable=true)
    private String provider;

    @Column(name="provider_id", nullable=true)
    private String providerID;


    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public User(
            String username,
            String email,
            String nickname,
            String password,
            Role role,
            String provider,
            String providerID
    ) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.providerID = providerID;

    }
}
