package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.service.TransactionService;
import com.checkhouse.core.dto.TransactionDTO;
import com.checkhouse.core.dto.request.TransactionRequest;

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

@Slf4j
@Tag(name = "transaction apis", description = "거래 관련 API - 거래 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "거래 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
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
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@GetMapping
	public BaseResponse<TransactionDTO> getTransaction(
		@Valid @RequestBody TransactionRequest.GetTransactionStatusRequest req
	) {
		log.info("[거래 조회] request: {}", req);
		TransactionDTO transaction = transactionService.getTransactionStatus(req);
		return BaseResponse.onSuccess(transaction);
	}

	@Operation(summary = "거래 상태 변경")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "변경 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@PutMapping
	public BaseResponse<TransactionDTO> updateTransaction(
		@Valid @RequestBody TransactionRequest.UpdateTransactionRequest req
	) {
		log.info("[거래 상태 변경] request: {}", req);
		TransactionDTO transaction = transactionService.updateTransactionStatus(req);
		return BaseResponse.onSuccess(transaction);
	}
}
