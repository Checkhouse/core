package com.checkhouse.core.entity;

import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update user us set us.deleted_at = now() where us.user_id = :userId")
@SQLRestriction("deleted_at is null")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="user_id")
    private UUID userId;

    @Column(
            name="user_name",
            nullable = false
    )
    private String username;

    @Column(
            name="email",
            nullable = false
    )
    private String email;

    @Column(name="nickname")
    private String nickname = null;

    @Column(name="password")
    private String password = null;

    @Column(name="provider")
    private String provider = null;

    @Column(name="provider_id")
    private String providerId = null;

    @Column(name="profile_image")
    private String profileImageURL;

    @Column(
            name="is_active",
            nullable=false
    )
    private Boolean isActive = true;

    @Column(
            name="role",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public User(
            UUID userId,
            String username,
            String email,
            String nickname,
            String password,
            String provider,
            String providerId,
            String profileImageURL,
            Boolean isActive,
            Role role
    ) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImageURL = profileImageURL;
        this.isActive = isActive;
        this.role = role;

    }

    public UserDTO toDto() {
        return new UserDTO(
                this.userId,
                this.username,
                this.email,
                this.nickname,
                this.password,
                this.provider,
                this.providerId,
                this.isActive,
                this.role
        );
    }

    public void updateUserState(Boolean state) {
        this.isActive = state;
    }

    public void updateUserInfo(
            String name,
            String nickname
    ) {
        this.username = name;
        this.nickname = nickname;
    }
}
