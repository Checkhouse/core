package com.checkhouse.core.dto.request;

import java.util.UUID;

public class TransactionRequest {
	public record AddTransactionRequest(
			UUID transactionId,
			UUID usedProductId,
			UUID buyerId
	){ }

	public record GetTransactionStatusRequest(
			UUID transactionId
	){ }

	public record UpdateTransactionRequest(
			UUID transactionId
	){ }

	public record GetTransactionsByUserRequest(
			UUID userId
	){ }
}
