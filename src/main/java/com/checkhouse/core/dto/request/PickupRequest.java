package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.Pickup;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.Store;
import com.checkhouse.core.entity.User;
import lombok.Builder;

import java.util.UUID;

public class PickupRequest {
	@Builder
	public record AddPickUpRequest(
			Transaction transaction,
			Store store
	){ }

	@Builder
	public record GetUserPickupListRequest(
			User user
	){ }

	@Builder
	public record GetUserPickupDetailsRequest(
			Pickup pickup,
			User user
	){ }

	@Builder
	public record UpdatePickUpRequest(
			Pickup pickup
	){ }
}
