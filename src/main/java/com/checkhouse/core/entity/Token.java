package com.checkhouse.core.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;

@RedisHash("token")
@AllArgsConstructor
@Getter
public class Token {
    @Id
    private UUID userId;

    private String act;

    private String rtk;

    @TimeToLive
    private Long expiration;
}
