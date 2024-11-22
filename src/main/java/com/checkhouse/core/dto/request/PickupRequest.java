package com.checkhouse.core.dto.request;

import lombok.Builder;

import java.util.UUID;

public class PickupRequest {
	@Builder
	public record AddPickUpRequest(
			UUID transactionId,
			UUID storeId
	){ }

	@Builder
	public record GetUserPickupListRequest(
			UUID userId
	){ }

	@Builder
	public record GetUserPickupDetailsRequest(
			UUID pickupId,
			UUID userId
	){ }

	@Builder
	public record UpdatePickUpRequest(
			UUID pickupId
	){ }

	@Builder
	public record UpdatePickUpForAdminRequest(
			UUID pickupId,
			UUID storeId,
			String code
	){ }
}
