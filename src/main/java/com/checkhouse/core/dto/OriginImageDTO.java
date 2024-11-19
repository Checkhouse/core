package com.checkhouse.core.dto;

import com.checkhouse.core.entity.ImageURL;
import com.checkhouse.core.entity.OriginProduct;

import java.util.UUID;

public record OriginImageDTO(
        UUID originImageId,
        ImageURL image,
        OriginProduct originProduct
) {}