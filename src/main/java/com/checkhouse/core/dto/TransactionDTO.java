package com.checkhouse.core.dto;

import java.util.UUID;

public record TransactionDTO(
	UUID transactionId,
	UsedProductDTO usedProduct,
	UserDTO buyer,
	Boolean isCompleted
) {
}
