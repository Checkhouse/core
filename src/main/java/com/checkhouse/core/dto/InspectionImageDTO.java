package com.checkhouse.core.dto;

import java.util.UUID;

public record InspectionImageDTO(
        UUID inspectionId,
        ImageDTO image,
        InspectionDTO inspection,
        UsedImageDTO usedImage
) {}