package com.checkhouse.core.service;
import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;

import com.checkhouse.core.dto.request.PickupRequest;
import com.checkhouse.core.dto.request.StoreRequest;
import com.checkhouse.core.dto.request.TransactionRequest;
import com.checkhouse.core.repository.mysql.PickupRepository;
import com.checkhouse.core.repository.mysql.TransactionRepository;
import com.checkhouse.core.repository.mysql.StoreRepository;
import com.checkhouse.core.dto.PickupDTO;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.Pickup;
import com.checkhouse.core.entity.Store;
import com.checkhouse.core.entity.User;

import com.checkhouse.core.repository.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@SQLDelete(sql="")
public class PickupService {
	private final PickupRepository pickupRepository;
	private final TransactionRepository transactionRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;

	public PickupDTO addUserPickup(PickupRequest.AddPickUpRequest request) {
		// 거래 존재 여부 확인
		Transaction transaction = transactionRepository.findById(request.transactionId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._TRANSACTION_NOT_FOUND));
		// 사용자 존재 여부 확인
		User user = userRepository.findById(transaction.getBuyer().getUserId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._PICKUP_USER_NOT_FOUND));
		// 스토어 존재 여부 확인
		Store store = storeRepository.findById(request.storeId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._PICKUP_STORE_NOT_FOUND));

		// 픽업 생성
		Pickup pickup = pickupRepository.save(
				Pickup.builder()
						.transaction(transaction)
						.store(store)
						.isPicked_up(false)
						.build()
		);

		return pickup.toDto();
	}

	public List<PickupDTO> getUserPickupList(PickupRequest.GetUserPickupListRequest request) {
		// 사용자 존재 여부 확인
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._PICKUP_USER_NOT_FOUND));

		return pickupRepository.findByUserId(user.getUserId())
				.stream()
				.map(Pickup::toDto)
				.collect(Collectors.toList());
	}

	public PickupDTO getUserPickupDetails(PickupRequest.GetUserPickupDetailsRequest request) {
		// 픽업 존재 여부 확인
		Pickup pickup = pickupRepository.findById(request.pickupId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._PICKUP_NOT_FOUND));
		// 사용자 존재 여부 확인
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._PICKUP_USER_NOT_FOUND));
		// 사용자와 픽업 사용자 일치 여부 확인
		if (!pickup.getTransaction().getBuyer().getUserId().equals(user.getUserId())) {
			throw new GeneralException(ErrorStatus._PICKUP_USER_NOT_FOUND);
		}
		return pickup.toDto();
	}

	public PickupDTO updatePickup(PickupRequest.UpdatePickUpRequest request) {
		// 픽업 존재 여부 확인
		Pickup pickup = pickupRepository.findById(request.pickupId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._PICKUP_NOT_FOUND));
		// 거래 존재 여부 확인
		Transaction transaction = transactionRepository.findById(pickup.getTransaction().getTransactionId())
				.orElseThrow(() -> new GeneralException(ErrorStatus._TRANSACTION_NOT_FOUND));
		// 이미 완료된 픽업인지 확인
		if (pickup.getIsPicked_up()) {
			throw new GeneralException(ErrorStatus._PICKUP_ALREADY_COMPLETED);
		}
		// 픽업 상태 변경
		pickup.updateState();
		// Pickup 저장
		pickupRepository.save(pickup);

		return pickup.toDto();
	}

	 public List<PickupDTO> getPickupListForAdmin(PickupRequest.GetPickUpListForAdminRequest request) {
		return pickupRepository.findByStoreId(request.storeID())
				.stream()
				.map(Pickup::toDto)
				.collect(Collectors.toList());
	}
}
