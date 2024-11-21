package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import lombok.Builder;

public class TransactionRequest {
	@Builder
	public record AddTransactionRequest(
		UsedProduct usedProduct,
		User buyer
	){ }
	@Builder
	public record GetTransactionStatusRequest(
		Transaction transaction
	){ }
	@Builder
	public record UpdateTransactionRequest(
		Transaction transaction
	){ }
	@Builder
	public record GetTransactionsByUserRequest(
		User user
	){ }
	@Builder
	public record GetTransactionsForAdminRequest(
		User admin
	){ }
}
