package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.Address;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Modifying
    @Query("DELETE FROM Address a WHERE a.name = :name")
    void hardDeleteByNameIfSoftDeleted(String name);
}

