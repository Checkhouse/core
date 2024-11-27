package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
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

    void checkURL(String URL) {}

    public ImageDTO addImage(ImageRequest.AddImageRequest req) {
        // URL 유효성 검사
        try {
            new URL(req.imageURL()).toURI();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._IMAGE_URL_NOT_EXIST);
        }
        ImageURL imageURL = imageRepository.save(
                ImageURL.builder()
                        .imageId(UUID.randomUUID())
                        .imageURL(req.imageURL())
                        .build()
        );
        return imageURL.toDto();
    }

    public ImageDTO getImage(ImageRequest.GetImageRequest req) {
        ImageURL imageURL = imageRepository.findById(req.imageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._IMAGE_ID_NOT_FOUND)
        );
        return imageURL.toDto();
    }
    public void deleteImage(ImageRequest.DeleteImageRequest req) {
        ImageURL imageURL = imageRepository.findById(req.imageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._IMAGE_ID_NOT_FOUND)
        );
        imageRepository.delete(imageURL);
    }

    //원본 이미지
    public OriginImageDTO addOriginImage(ImageRequest.AddOriginImageRequest req) {
        OriginProduct originProduct = originProductRepository.findById(req.originProductId()).orElseThrow(
                () -> {throw new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND);}
        );
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
        return originImage.toDto();
    }
    public OriginImageDTO getOriginImage(ImageRequest.GetOriginImageRequest req) {
        OriginImage originImage = originImageRepository.findById(req.originImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_IMAGE_ID_NOT_FOUND)
        );
        return originImage.toDto();
    }
    public List<OriginImageDTO> getOriginImagesByOriginId(ImageRequest.GetOriginImagesByOriginIdRequest req) {
        originProductRepository.findById(req.originProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND)
        );
        return originImageRepository.findOriginImagesByOriginProductOriginProductId(req.originProductId())
                .stream()
                .map(OriginImage::toDto)
                .toList();
    }
    public void deleteOriginImage(ImageRequest.DeleteOriginImageRequest req) {
        OriginImage originImage = originImageRepository.findById(req.originImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_IMAGE_ID_NOT_FOUND)
        );
        originImageRepository.delete(originImage);
    }

    //중고 이미지
    public UsedImageDTO addUsedImage(ImageRequest.AddUsedImageRequest req) {
        UsedProduct usedProduct = usedProductRepository.findById(req.usedProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND)
        );
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
        return usedImage.toDto();
    }
    public UsedImageDTO getUsedImage(ImageRequest.GetUsedImageRequest req) {
        UsedImage usedImage = usedImageRepository.findById(req.usedImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_IMAGE_ID_NOT_FOUND)
        );
        return usedImage.toDto();
    }
    public List<UsedImageDTO> getUsedImagesByUsedId(ImageRequest.GetUsedImagesByUsedIdRequest req) {
        usedProductRepository.findById(req.usedProductId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND)
        );
        return usedImageRepository.findUsedImagesByUsedProductUsedProductId(req.usedProductId())
                .stream()
                .map(UsedImage::toDto)
                .toList();
    }
    public void deleteUsedImage(ImageRequest.DeleteUsedImageRequest req) {
        UsedImage usedImage = usedImageRepository.findById(req.usedImageId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USED_IMAGE_ID_NOT_FOUND)
        );
        usedImageRepository.delete(usedImage);
    }

    //TODO: 검수이미지 추가

}
