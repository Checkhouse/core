package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.dto.InspectionImageDTO;
import com.checkhouse.core.dto.OriginImageDTO;
import com.checkhouse.core.dto.UsedImageDTO;
import com.checkhouse.core.entity.*;
import com.checkhouse.core.repository.mysql.*;
import com.checkhouse.core.dto.request.ImageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final OriginProductRepository originProductRepository;
    private final UsedProductRepository usedProductRepository;
    private final OriginImageRepository originImageRepository;
    private final UsedImageRepository usedImageRepository;
    private final InspectionImageRepository inspectionImageRepository;
    private final InspectionRepository inspectionRepository;

    void checkURL(String URL) {}

    ImageDTO addImage(ImageRequest.AddImageRequest req) {
        // URL 유효성 검사
        try {
            new URL(req.imageURL()).toURI();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._IMAGE_URL_NOT_EXIST);
        }
        ImageURL imageURL = imageRepository.save(
                ImageURL.builder()
                        .imageURL(req.imageURL())
                        .build()
        );
        return imageURL.toDto();
    }

    ImageDTO getImage(ImageRequest.GetImageRequest req) {
        ImageURL imageURL = imageRepository.findById(req.imageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._IMAGE_ID_NOT_FOUND)
        );
        return imageURL.toDto();
    }
    void deleteImage(ImageRequest.DeleteImageRequest req) {
        ImageURL imageURL = imageRepository.findById(req.imageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._IMAGE_ID_NOT_FOUND)
        );
        imageRepository.delete(imageURL);
    }

    //원본 이미지
    OriginImageDTO addOriginImage(ImageRequest.AddOriginImageRequest req) {
        OriginProduct originProduct = originProductRepository.findById(req.originProductId()).orElseThrow(
                () -> {throw new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND);}
        );
        ImageURL imageURL = imageRepository.save(
                ImageURL.builder()
                        .imageURL(req.imageURL())
                        .build()
        );
        OriginImage originImage = originImageRepository.save(
                OriginImage.builder()
                        .originProduct(originProduct)
                        .image(imageURL)
                        .build()
        );
        return originImage.toDto();
    }
    OriginImageDTO getOriginImage(ImageRequest.GetOriginImageRequest req) {
        OriginImage originImage = originImageRepository.findById(req.originImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_IMAGE_ID_NOT_FOUND)
        );
        return originImage.toDto();
    }
    List<OriginImageDTO> getOriginImagesByOriginId(ImageRequest.GetOriginImagesByOriginIdRequest req) {
        originProductRepository.findById(req.originProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND)
        );
        return originImageRepository.findOriginImagesByOriginProductOriginProductId(req.originProductId())
                .stream()
                .map(OriginImage::toDto)
                .toList();
    }
    void deleteOriginImage(ImageRequest.DeleteOriginImageRequest req) {
        OriginImage originImage = originImageRepository.findById(req.originImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_IMAGE_ID_NOT_FOUND)
        );
        originImageRepository.delete(originImage);
    }

    //중고 이미지
    UsedImageDTO addUsedImage(ImageRequest.AddUsedImageRequest req) {
        UsedProduct usedProduct = usedProductRepository.findById(req.usedProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND)
        );
        ImageURL imageURL = imageRepository.save(
                ImageURL.builder()
                        .imageURL(req.imageURL())
                        .build()
        );
        UsedImage usedImage = usedImageRepository.save(
                UsedImage.builder()
                        .usedImageId(req.usedImageId())
                        .usedProduct(usedProduct)
                        .image(imageURL)
                        .build()
        );
        return usedImage.toDto();
    }
    UsedImageDTO getUsedImage(ImageRequest.GetUsedImageRequest req) {
        UsedImage usedImage = usedImageRepository.findById(req.usedImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_IMAGE_ID_NOT_FOUND)
        );
        return usedImage.toDto();
    }
    List<UsedImageDTO> getUsedImagesByUsedId(ImageRequest.GetUsedImagesByUsedIdRequest req) {
        usedProductRepository.findById(req.usedProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND)
        );
        return usedImageRepository.findUsedImagesByUsedProductUsedProductId(req.usedProductId())
                .stream()
                .map(UsedImage::toDto)
                .toList();
    }
    void deleteUsedImage(ImageRequest.DeleteUsedImageRequest req) {
        UsedImage usedImage = usedImageRepository.findById(req.usedImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_IMAGE_ID_NOT_FOUND)
        );
        usedImageRepository.delete(usedImage);
    }

    InspectionImageDTO addInspectionImage(ImageRequest.AddInspectionImageRequest req) {
        // 검수 ID 유효성 검사
        Inspection inspection = inspectionRepository.findById(req.inspectionId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_ID_NOT_FOUND));

        // 중고 이미지 ID 유효성 검사
        UsedImage usedImage = usedImageRepository.findById(req.usedImageId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_IMAGE_ID_NOT_FOUND));

        // 중복된 검수 이미지가 이미 존재하는지 검사
        inspectionImageRepository.findInspectionImageByUsedImageUsedImageId(req.usedImageId())
                .ifPresent(existing -> {
                    throw new GeneralException(ErrorStatus._INSPECTION_IMAGE_ALREADY_EXISTS);
                });

        // UsedProduct 일치 여부 확인
        if (!inspection.getUsedProduct().getUsedProductId().equals(usedImage.getUsedProduct().getUsedProductId())) {
            throw new GeneralException(ErrorStatus._INSPECTION_IMAGE_PRODUCT_NOT_MATCH);
        }

        // 이미지 URL 저장
        ImageURL imageURL = imageRepository.save(
                ImageURL.builder()
                        .imageURL(req.imageURL())
                        .build()
        );

        // 검수 이미지 저장
        InspectionImage inspectionImage = inspectionImageRepository.save(
                InspectionImage.builder()
                        .inspection(inspection)
                        .usedImage(usedImage)
                        .image(imageURL)
                        .build()
        );

        return inspectionImage.toDto();
    }

    InspectionImageDTO getInspectionImage(ImageRequest.GetInspectionImageRequest req) {
        InspectionImage inspectionImage = inspectionImageRepository.findById(req.inspectionImageId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_IMAGE_ID_NOT_FOUND));
        return inspectionImage.toDto();
    }
    InspectionImageDTO getInspectionImageByUsedImageId(ImageRequest.GetInspectionImageByUsedImageIdRequest req) {
        usedImageRepository.findById(req.usedImageId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_IMAGE_ID_NOT_FOUND));

        InspectionImage inspectionImage = inspectionImageRepository.findInspectionImageByUsedImageUsedImageId(req.usedImageId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_IMAGE_ID_NOT_FOUND));

        return inspectionImage.toDto();
    }
    List<InspectionImageDTO> getInspectionImagesByInspectionId(ImageRequest.GetInspectionImagesByInspectionIdRequest req) {
        Inspection inspection = inspectionRepository.findById(req.inspectionId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_ID_NOT_FOUND));

        List<InspectionImage> inspectionImages = inspectionImageRepository.findInspectionImagesByInspectionInspectionId(req.inspectionId());
        return inspectionImages.stream().map(InspectionImage::toDto).toList();
    }
    void deleteInspectionImage(ImageRequest.DeleteInspectionImageRequest req) {
        InspectionImage inspectionImage = inspectionImageRepository.findById(req.inspectionId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_IMAGE_ID_NOT_FOUND));
        inspectionImageRepository.delete(inspectionImage);
    }


}
