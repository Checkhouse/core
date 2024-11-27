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
@RequestMapping("api/pickup")
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
		try {
			log.info("[픽업 등록] request: {}", req);
			return BaseResponse.onSuccess(pickupService.addUserPickup(req));
		} catch (GeneralException e) {
			log.error("[픽업 등록] request: {}, error: {}", req, e.getMessage());
			return BaseResponse.onFailure(e.getCode().toString(), e.getMessage(), null);
		}	
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
		try {
			PickupRequest.GetUserPickupListRequest req = new PickupRequest.GetUserPickupListRequest(userId);
			log.info("[사용자 픽업 조회] request: {}", req);
			return BaseResponse.onSuccess(pickupService.getUserPickupList(req));
		} catch (GeneralException e) {
			log.error("[사용자 픽업 조회] userId: {}, error: {}", userId, e.getMessage());
			return BaseResponse.onFailure(e.getCode().toString(), e.getMessage(), null);
		}
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
		try {
			String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			UUID userId = userService.getUserInfo(userEmail).userId();
			PickupRequest.GetUserPickupDetailsRequest req = new PickupRequest.GetUserPickupDetailsRequest(
				pickupId,
				userId
			);
			log.info("[픽업 조회] request: {}", req);
			return BaseResponse.onSuccess(pickupService.getUserPickupDetails(req));
		} catch (GeneralException e) {
			log.error("[픽업 조회] pickupId: {}, error: {}", pickupId, e.getMessage());
			return BaseResponse.onFailure(e.getCode().toString(), e.getMessage(), null);
		}
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
		try {
			// 관리자 코드 확인
			if (storeService.verifyCode(new StoreRequest.VerifyCodeRequest(
				req.storeId(),
				req.code()
			))) {
				log.info("[픽업 상태 변경] request: {}", req);
				PickupDTO pickup = pickupService.updatePickup(req);
				// 거래 성공으로 변경
				try {
					transactionService.updateTransactionStatus(new TransactionRequest.UpdateTransactionRequest(
						pickup.transaction().transactionId()
					));
				} catch (Exception e) {
					log.error("[거래 상태 변경] transactionId: {}, error: {}", pickup.transaction().transactionId(), e.getMessage());
				}
				return BaseResponse.onSuccess(pickup);
			} else {
				return BaseResponse.onFailure("", "코드가 일치하지 않습니다.", null);
			}
		} catch (GeneralException e) {
			log.error("[픽업 상태 변경] pickupId: {}, error: {}", req.pickupId()
			, e.getMessage());
			return BaseResponse.onFailure(e.getCode().toString(), e.getMessage(), null);
		}
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
		try {
			// 관리자 확인
			UserDTO user = userService.getUserInfo(SecurityContextHolder.getContext().getAuthentication().getName());
			if (user.role().equals(Role.ROLE_ADMIN)) {
				log.info("[관리자 픽업 확인] request: {}", req);
				return BaseResponse.onSuccess(pickupService.getPickupListForAdmin(req));
			} else {
				return BaseResponse.onFailure("", "권한이 없습니다.", null);
			}
		} catch (GeneralException e) {
			log.error("[관리자 픽업 확인] request: {}, error: {}", req, e.getMessage());
			return BaseResponse.onFailure(e.getCode().toString(), e.getMessage(), null);
		}
	}
}
