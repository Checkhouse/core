package com.checkhouse.core.dto.request;

import java.util.UUID;

public class PickupRequest {
	public record AddPickUpRequest(
			UUID pickupId,
			UUID transactionId,
			UUID storeId
	){ }

	public record GetUserPickupListRequest(
			UUID userId
	){ }

	public record GetUserPickupDetailsRequest(
			UUID pickupId,
			UUID userId
	){ }

	public record UpdatePickUpRequest(
			UUID pickupId
	){ }
}
