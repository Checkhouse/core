package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.PurchasedProductDTO;
import com.checkhouse.core.dto.UsedImageDTO;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.UsedImage;
import com.checkhouse.core.repository.mysql.UsedImageRepository;
import com.checkhouse.core.service.TransactionService;
import com.checkhouse.core.dto.TransactionDTO;
import com.checkhouse.core.dto.request.TransactionRequest;

import com.checkhouse.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "transaction apis", description = "거래 관련 API - 거래 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;
	private final UsedImageRepository usedImageRepository;
	private final UserService userService;

    @Operation(summary = "거래 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공")
    })
    @PostMapping
    public BaseResponse<TransactionDTO> addTransaction(
        @Valid @RequestBody TransactionRequest.AddTransactionRequest req
    ) 
	{
		log.info("[거래 등록] request: {}", req);
		TransactionDTO transaction = transactionService.addTransaction(req);
		return BaseResponse.onSuccess(transaction);
    }

	@Operation(summary = "거래 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공")
	})
	@GetMapping
	public BaseResponse<TransactionDTO> getTransaction(
		@RequestParam UUID transactionId
	) {
		TransactionRequest.GetTransactionStatusRequest req = TransactionRequest.GetTransactionStatusRequest.builder()
			.transactionId(transactionId)
			.build();
		log.info("[거래 조회] request: {}", req);
		TransactionDTO transaction = transactionService.getTransactionStatus(req);
		return BaseResponse.onSuccess(transaction);
	}

	@Operation(summary = "거래 상태 변경")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "변경 성공")
	})
	@PatchMapping
	public BaseResponse<TransactionDTO> updateTransaction(
		@Valid @RequestBody TransactionRequest.UpdateTransactionRequest req
	) {
		log.info("[거래 상태 변경] request: {}", req);
		TransactionDTO transaction = transactionService.updateTransactionStatus(req);
		return BaseResponse.onSuccess(transaction);
	}

	@Operation(summary = "구매한 상품 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공")
	})
	@GetMapping("/purchased")
	public BaseResponse<List<PurchasedProductDTO>> getPurchasedProducts() {
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		UUID userId = userService.getUserInfo(userEmail).userId();
		TransactionRequest.GetPurchasedProductsRequest req = TransactionRequest.GetPurchasedProductsRequest.builder()
			.userId(userId)
			.build();
		log.info("[구매한 상품 조회] request: {}", req);
		List<TransactionDTO> transactions = transactionService.getPurchasedProducts(req);

		return BaseResponse.onSuccess(
				transactions
						.stream()
				.map(this::convertToDTO)
		.collect(Collectors.toList()));
	}
	private PurchasedProductDTO convertToDTO(TransactionDTO transaction) {
		List<UsedImageDTO> images = usedImageRepository.findUsedImagesByUsedProductUsedProductId(transaction.usedProduct().usedProductId())
				.stream()
				.map(UsedImage::toDto)
				.collect(Collectors.toList());

		return new PurchasedProductDTO(
				transaction.transactionId(),
				transaction.usedProduct().usedProductId(),
				transaction.usedProduct().title(),
				transaction.usedProduct().description(),
				transaction.usedProduct().price(),
				images
		);
	}
}
