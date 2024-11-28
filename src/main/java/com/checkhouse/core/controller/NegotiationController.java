package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.request.NegotiationRequest;
import com.checkhouse.core.dto.NegotiationDTO;
import com.checkhouse.core.dto.request.TransactionRequest;
import com.checkhouse.core.service.NegotiationService;
import com.checkhouse.core.service.TransactionService;
import com.checkhouse.core.service.UserService;

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

import java.util.List;
import java.util.UUID;


@Slf4j
@Tag(name = "negotiation apis", description = "네고 관련 API - 네고 등록, 수정, 삭제, 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/negotiation")
public class NegotiationController {
    private final NegotiationService negotiationService;
	private final TransactionService transactionService;
	private final UserService userService;

	@Operation(summary = "네고 등록")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "등록 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@PostMapping
	public BaseResponse<NegotiationDTO> addNegotiation(
		@Valid @RequestBody NegotiationRequest.AddNegotiationRequest req
	) {
		log.info("[네고 등록] request: {}", req);
		return BaseResponse.onSuccess(negotiationService.addNegotiation(req));
	}
	// 네고 상태 변경
	@Operation(summary = "네고 상태 변경")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "변경 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@PatchMapping("/{negotiationId}")
	public BaseResponse<NegotiationDTO> updateNegotiation(
		@PathVariable UUID negotiationId,
		@Valid @RequestBody NegotiationRequest.UpdateNegotiationRequest req
	) {
		log.info("[네고 상태 변경] request: {}", req);
		// jwt 토큰에서 유저 아이디 추출
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		UUID userId = userService.getUserInfo(userEmail).userId();
		NegotiationRequest.UpdateNegotiationRequest request = new NegotiationRequest.UpdateNegotiationRequest(
			negotiationId,
			req.state()
		);

		switch (req.state()) {
			case ACCEPTED: // 네고 승인, 거래 생성
				NegotiationDTO negotiation = negotiationService.updateNegotiationState(request);
				transactionService.addTransaction(new TransactionRequest.AddTransactionRequest(
					negotiation.usedProduct().usedProductId(),
					negotiation.buyer().userId()
				));
				return BaseResponse.onSuccess(negotiation);
			default:
				return BaseResponse.onSuccess(negotiationService.updateNegotiationState(request));
		}
	}
	// 제안한 네고 조회
	@Operation(summary = "제안한 네고 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@GetMapping("/buy")
	public BaseResponse<List<NegotiationDTO>> getNegotiationByBuyer() {
		// jwt 토큰에서 유저 아이디 추출
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		UUID buyerId = userService.getUserInfo(userEmail).userId();
		log.info("[제안한 네고 조회] buyerId: {}", buyerId.toString());
		NegotiationRequest.GetNegotiationByBuyerRequest getNegotiationByBuyerRequest = new NegotiationRequest.GetNegotiationByBuyerRequest(buyerId);
		return BaseResponse.onSuccess(negotiationService.getNegotiationByBuyer(getNegotiationByBuyerRequest));
	}
	// 받은 네고 조회
	@Operation(summary = "받은 네고 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@GetMapping("/sell")
	public BaseResponse<List<NegotiationDTO>> getNegotiationBySeller() {
		// jwt 토큰에서 유저 아이디 추출
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		UUID sellerId = userService.getUserInfo(userEmail).userId();
		log.info("[받은 네고 조회] sellerId: {}", sellerId.toString());
		NegotiationRequest.GetNegotiationBySellerRequest getNegotiationBySellerRequest = new NegotiationRequest.GetNegotiationBySellerRequest(sellerId);
		return BaseResponse.onSuccess(negotiationService.getNegotiationBySeller(getNegotiationBySellerRequest));
	}
}
