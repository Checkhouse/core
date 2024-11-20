package com.checkhouse.core.repository.mysql;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkhouse.core.entity.Send;

public interface SendRepository extends JpaRepository<Send, UUID> {
    Optional<Send> findBySendId(UUID sendId);
}
