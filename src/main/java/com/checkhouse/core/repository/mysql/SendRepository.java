package com.checkhouse.core.repository.mysql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkhouse.core.entity.Send;
import com.checkhouse.core.entity.Transaction;

public interface SendRepository extends JpaRepository<Send, UUID> {
    Optional<Send> findBySendId(UUID sendId);
    Optional<Send> findByTransaction(Transaction transaction);
    List<Send> findByTransaction(UUID transactionId);
}
