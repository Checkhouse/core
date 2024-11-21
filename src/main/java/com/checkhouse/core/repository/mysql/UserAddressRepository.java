package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    List<UserAddress> findAllByUserId(UUID userId);
}
