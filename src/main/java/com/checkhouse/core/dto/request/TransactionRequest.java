package com.checkhouse.core.dto.request;


import lombok.Builder;

import java.util.UUID;

public class TransactionRequest {
	@Builder
	public record AddTransactionRequest(
		UUID usedProductId,
		UUID buyerId
	){ }
	@Builder
	public record GetTransactionStatusRequest(
		UUID transactionId
	){ }
	@Builder
	public record UpdateTransactionRequest(
		UUID transactionId
	){ }
	@Builder
	public record GetTransactionsByUserRequest(
		UUID userId
	){ }
	@Builder
	public record GetTransactionsForAdminRequest(
		UUID adminId
	){ }
}
