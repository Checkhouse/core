package com.checkhouse.core.dto;

import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;

import java.util.UUID;

public record TransactionDTO(
	UUID transactionId,
	UsedProduct usedProduct,
	User buyer,
	Boolean isCompleted
) {}