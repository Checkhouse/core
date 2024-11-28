package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.PickupDTO;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.PickupRequest;
import com.checkhouse.core.dto.request.StoreRequest;
import com.checkhouse.core.dto.request.TransactionRequest;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.service.PickupService;
import com.checkhouse.core.service.StoreService;
import com.checkhouse.core.service.TransactionService;
import com.checkhouse.core.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "pickup apis", description = "픽업 관련 API - 픽업 등록, 조회")
@RestController
@RequestMapping("api/v1/pickup")
@RequiredArgsConstructor
public class PickupController {
	private final PickupService pickupService;
	private final TransactionService transactionService;
	private final UserService userService;
	private final StoreService storeService;

	@PostMapping
	@Operation(summary = "픽업 등록")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "등록 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	public BaseResponse<PickupDTO> addPickup(
		@Valid @RequestBody PickupRequest.AddPickUpRequest req
	) {
		log.info("[픽업 등록] request: {}", req);
		return BaseResponse.onSuccess(pickupService.addUserPickup(req));
	}

	@GetMapping("/{userId}")
	@Operation(summary = "사용자 픽업 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	public BaseResponse<List<PickupDTO>> getPickupList(
		@PathVariable UUID userId
	) {
		PickupRequest.GetUserPickupListRequest req = new PickupRequest.GetUserPickupListRequest(userId);
		log.info("[사용자 픽업 조회] request: {}", req);
		return BaseResponse.onSuccess(pickupService.getUserPickupList(req));
	}

	@GetMapping("/{pickupId}")
	@Operation(summary = "특정 픽업 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	public BaseResponse<PickupDTO> getPickupDetails(
		@PathVariable UUID pickupId
	) {
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		UUID userId = userService.getUserInfo(userEmail).userId();
		PickupRequest.GetUserPickupDetailsRequest req = new PickupRequest.GetUserPickupDetailsRequest(
			pickupId,
			userId
		);
		log.info("[픽업 조회] request: {}", req);
		return BaseResponse.onSuccess(pickupService.getUserPickupDetails(req));
	}

	@PatchMapping("/{pickupId}")
	@Operation(summary = "픽업 상태 변경")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "변경 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	public BaseResponse<PickupDTO> verifyPickup(
		@Valid @RequestBody PickupRequest.UpdatePickUpRequest req
	) {
		// 관리자 코드 확인
		storeService.verifyCode(new StoreRequest.VerifyCodeRequest(
			req.storeId(),
			req.code()
		));
		log.info("[픽업 상태 변경] request: {}", req);
		PickupDTO pickup = pickupService.updatePickup(req);
		// 거래 성공으로 변경
		transactionService.updateTransactionStatus(new TransactionRequest.UpdateTransactionRequest(
			pickup.transaction().transactionId()
		));
		return BaseResponse.onSuccess(pickup);
	}

	@PatchMapping("/admin")
	@Operation(summary = "관리자 픽업 확인")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "확인 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	public BaseResponse<List<PickupDTO>> getStorePickup(
		@Valid @RequestBody PickupRequest.GetPickUpListForAdminRequest req
	) {
		// 관리자 확인
		UserDTO user = userService.getUserInfo(SecurityContextHolder.getContext().getAuthentication().getName());
		if (user.role().equals(Role.ROLE_ADMIN)) {
			log.info("[관리자 픽업 확인] request: {}", req);
			return BaseResponse.onSuccess(pickupService.getPickupListForAdmin(req));
		} else {
			return BaseResponse.onFailure("", "권한이 없습니다.", null);
		}
	}
}
