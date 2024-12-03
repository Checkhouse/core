package com.checkhouse.core.dto;

import java.util.List;
import java.util.UUID;

//public class PurchasedProductDTO {
//	private UUID transactionId;
//	private UUID usedProductId;
//	private String title;
//	private String description;
//	private int price;
//	private List<UsedImageDTO> images;
//
//	public PurchasedProductDTO() {}
//
//	public PurchasedProductDTO(
//			UUID transactionId,
//			UUID usedProductId,
//			String title,
//			String description,
//			int price,
//			List<UsedImageDTO> images
//			)
//	{
//		this.transactionId = transactionId;
//		this.usedProductId = usedProductId;
//		this.title = title;
//		this.description = description;
//		this.price = price;
//		this.images = images;
//	}
//}

public record PurchasedProductDTO(
		UUID transactionId,
		UUID usedProductId,
		String title,
		String description,
		int price,
		List<UsedImageDTO> images
){}
