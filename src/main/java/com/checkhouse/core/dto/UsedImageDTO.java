package com.checkhouse.core.dto;

import com.checkhouse.core.entity.ImageURL;
import com.checkhouse.core.entity.UsedProduct;

import java.util.UUID;

public record UsedImageDTO(
        UUID usedImageId,
        ImageURL image,
        UsedProduct usedProduct
) {}