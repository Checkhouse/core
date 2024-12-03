package com.checkhouse.core.repository.mysql;


import com.checkhouse.core.dto.PurchasedProductDTO;
import com.checkhouse.core.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	@Query("SELECT DISTINCT t FROM Transaction t " +
			"JOIN FETCH t.usedProduct u " +
//			"LEFT JOIN FETCH UsedImage ui ON ui.usedProduct.usedProductId = u.usedProductId " +
			"WHERE t.buyer.userId = :userId AND t.isCompleted = true")
	List<Transaction> findPurchasedProductsByUserId(@Param("userId") UUID userId);

}
