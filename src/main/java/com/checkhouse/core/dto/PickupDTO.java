package com.checkhouse.core.dto;

import java.util.UUID;

public record PickupDTO(
	UUID pickupId,
	UUID transactionId,
	UUID storeId,
	Boolean isPicked_up
) {}
