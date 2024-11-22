package com.checkhouse.core.service;
import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;

import com.checkhouse.core.dto.TransactionDTO;
import com.checkhouse.core.dto.request.TransactionRequest;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.repository.mysql.TransactionRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@SQLDelete(sql="")
public class TransactionService {
	private final TransactionRepository transactionRepository;
	private final UsedProductRepository usedProductRepository;
	private final UserRepository userRepository;

	// 거래 등록
	public TransactionDTO addTransaction(TransactionRequest.AddTransactionRequest request) {
		// 중고 상품 확인
		UsedProduct usedProduct = usedProductRepository
				.findById(request.usedProductId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND));
		// 이미 거래된 상품인지 확인 
		transactionRepository.findByUsedProduct(usedProduct.getUsedProductId())
				.ifPresent(transaction -> {
					throw new GeneralException(ErrorStatus._TRANSACTION_ALREADY_EXISTS);
				});
		// 구매자 확인
		User buyer = userRepository.findById(request.buyerId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
		// 거래 등록
		// toDo 픽업, 딜리버리 생성
		Transaction savedTransaction = transactionRepository.save(
			Transaction.builder()
					.usedProduct(usedProduct)
					.buyer(buyer)
					.isCompleted(false)
					.build()
		);

		return savedTransaction.toDto();
	}

	// 거래 상태 조회
	public TransactionDTO getTransactionStatus(TransactionRequest.GetTransactionStatusRequest request) {
		return transactionRepository.findById(request.transactionId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._TRANSACTION_NOT_FOUND))
				.toDto();
	}

	// 거래 상태 변경
	public TransactionDTO updateTransactionStatus(TransactionRequest.UpdateTransactionRequest request) {
		Transaction transaction = transactionRepository.findById(request.transactionId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._TRANSACTION_NOT_FOUND));

		//상태 중복 체크
		if(transaction.getIsCompleted()){
			throw new GeneralException(ErrorStatus._TRANSACTION_STATE_CHANGE_FAILED);
		}
		// 상태 변경
		transaction.updateStatus();
		return transaction.toDto();
	}

	public List<TransactionDTO> getTransactionsByUser(TransactionRequest.GetTransactionsByUserRequest request) {
		// 구매자 확인
		User buyer = userRepository.findById(request.userId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._TRANSACTION_USER_LIST_FAILED));
		
		return transactionRepository.findByBuyer(buyer.getUserId())
				.stream()
				.map(Transaction::toDto)
				.collect(Collectors.toList());
	}

	public List<TransactionDTO> getTransactionsForAdmin(TransactionRequest.GetTransactionsForAdminRequest request) {
		// 관리자 확인
		User admin = userRepository.findById(request.adminId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

		if(!admin.getRole().equals(Role.ROLE_ADMIN)){
			throw new GeneralException(ErrorStatus._USER_NOT_ADMIN);
		}

		return transactionRepository.findAll()
				.stream()
				.map(Transaction::toDto)
				.collect(Collectors.toList());
	}

}
