package com.checkhouse.core.dto;

import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import java.util.UUID;

public record TransactionDTO(
	UUID transactionId,
	UsedProductDTO usedProduct,
	UserDTO buyer,
	Boolean isCompleted
) {
}