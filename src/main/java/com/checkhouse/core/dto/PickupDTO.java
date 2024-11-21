package com.checkhouse.core.dto;

import java.util.UUID;

public record PickupDTO(
	UUID pickupId,
	TransactionDTO transaction,
	StoreDTO store,
	Boolean isPicked_up
) {}
