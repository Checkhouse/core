package com.checkhouse.core.repository.mysql;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkhouse.core.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    
}
