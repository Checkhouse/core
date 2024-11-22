package com.checkhouse.core.repository.mysql;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.checkhouse.core.entity.Pickup;
import org.springframework.data.jpa.repository.Query;

public interface PickupRepository extends JpaRepository<Pickup, UUID> {
	@Query("SELECT p FROM Pickup p WHERE p.transaction.buyer.userId = :userId")
	List<Pickup> findByUserId(UUID userId);
}
