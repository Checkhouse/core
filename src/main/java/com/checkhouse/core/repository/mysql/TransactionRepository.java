package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
	//findByBuyer
	@Query("SELECT t FROM Transaction t WHERE t.buyer.userId = :userId")
	List<Transaction> findByBuyer(UUID userId);

	//findByUsedProduct
	@Query("SELECT t FROM Transaction t WHERE t.usedProduct.usedProductId = :usedProductId")
	Optional<Transaction> findByUsedProduct(UUID usedProductId);
}
