package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.dto.OriginImageDTO;
import com.checkhouse.core.dto.UsedImageDTO;
import com.checkhouse.core.dto.UsedProductDTO;
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

    void checkURL(String url) {
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._IMAGE_URL_NOT_EXIST);
        }
    }

    ImageDTO AddImage(ImageRequest.AddImageRequest req) {
        // URL 유효성 검사
        checkURL(req.imageURL());
        ImageURL imageURL = imageRepository.save(
                ImageURL.builder()
                        .imageId(UUID.randomUUID())
                        .imageURL(req.imageURL())
                        .build()
        );
        return imageURL.toDTO();
    }

    ImageDTO GetImage(ImageRequest.GetImageRequest req) {
        ImageURL imageURL = imageRepository.findById(req.imageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._IMAGE_ID_NOT_FOUND)
        );
        return imageURL.toDTO();
    }
    void DeleteImage(ImageRequest.DeleteImageRequest req) {
        ImageURL imageURL = imageRepository.findById(req.imageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._IMAGE_ID_NOT_FOUND)
        );
        imageRepository.delete(imageURL);
    }

    //원본 이미지
    OriginImageDTO AddOriginImage(ImageRequest.AddOriginImageRequest req) {
        // 원본 물품 확인
        OriginProduct originProduct = originProductRepository.findById(req.originProduct().getOriginProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND)
        );

        // URL 유효성 검사
        checkURL(req.imageURL());


        // Save image URL and origin image
        ImageURL imageURL = imageRepository.save(
                ImageURL.builder()
                        .imageId(UUID.randomUUID())
                        .imageURL(req.imageURL())
                        .build()
        );

        OriginImage originImage = originImageRepository.save(
                OriginImage.builder()
                        .originImageId(req.originImageId())
                        .originProduct(originProduct)
                        .image(imageURL)
                        .build()
        );

        return originImage.toDTO();
    }
    OriginImageDTO GetOriginImage(ImageRequest.GetOriginImageRequest req) {
        OriginImage originImage = originImageRepository.findById(req.originImageId()).orElseThrow(
            () -> new GeneralException(ErrorStatus._ORIGIN_IMAGE_ID_NOT_FOUND)
        );
        return originImage.toDTO();
    }
    List<OriginImageDTO> GetOriginImagesByOriginId(ImageRequest.GetOriginImagesByOriginIdRequest req) {
        OriginProduct originProduct = originProductRepository.findById(req.originProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND)
        );

        List<OriginImage> originImages = originImageRepository.findUsedImagesByOriginProduct(originProduct);

        return originImages.stream().map(OriginImage::toDTO).toList();
    }
    void DeleteOriginImage(ImageRequest.DeleteOriginImageRequest req) {
        OriginImage originImage = originImageRepository.findById(req.originImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_IMAGE_ID_NOT_FOUND)
        );
        originImageRepository.delete(originImage);
    }

    //중고 이미지
    UsedImageDTO AddUsedImage(ImageRequest.AddUsedImageRequest req) {
        UsedProduct usedProduct = usedProductRepository.findById(req.usedProduct().getUsedProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND)
        );
        checkURL(req.imageURL());

        ImageURL imageURL = imageRepository.save(
                ImageURL.builder()
                        .imageId(UUID.randomUUID())
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

        return usedImage.toDTO();
    }
    UsedImageDTO GetUsedImage(ImageRequest.GetUsedImageRequest req) {
        UsedImage usedImage = usedImageRepository.findById(req.usedImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_IMAGE_ID_NOT_FOUND)
        );
        return usedImage.toDTO();
    }
    List<UsedImageDTO> GetUsedImagesByUsedId(ImageRequest.GetUsedImagesByUsedIdRequest req) {
        UsedProduct usedProduct = usedProductRepository.findById(req.usedProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND)
        );

        List<UsedImage> usedImages = usedImageRepository.findUsedImagesByUsedProduct(usedProduct);

        return usedImages.stream().map(UsedImage::toDTO).toList();
    }
    void DeleteUsedImage(ImageRequest.DeleteUsedImageRequest req) {
        UsedImage usedImage = usedImageRepository.findById(req.usedImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_IMAGE_ID_NOT_FOUND)
        );
        usedImageRepository.delete(usedImage);
    }

    //TODO: 검수이미지 추가

}
