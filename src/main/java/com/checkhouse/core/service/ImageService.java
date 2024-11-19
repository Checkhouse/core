package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.dto.OriginImageDTO;
import com.checkhouse.core.dto.UsedImageDTO;
import com.checkhouse.core.dto.UsedProductDTO;
import com.checkhouse.core.entity.ImageURL;
import com.checkhouse.core.entity.OriginImage;
import com.checkhouse.core.entity.OriginProduct;
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

    ImageDTO AddImage(ImageRequest.AddImageRequest req) {
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
        originProductRepository.findById(req.originImageId()).orElseThrow(
                () -> {throw new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND);}
        );
        return null;
    }
    OriginImageDTO GetOriginImage(ImageRequest.GetOriginImageRequest req) {return null;}
    List<OriginImageDTO> GetOriginImagesByOriginId(ImageRequest.GetOriginImagesByOriginIdRequest req) {return null;}
    void DeleteOriginImage(ImageRequest.DeleteOriginImageRequest req) {}

    //중고 이미지
    UsedImageDTO AddUsedImage(ImageRequest.AddUsedImageRequest req) {return null;}
    UsedImageDTO GetUsedImage(ImageRequest.GetUsedImageRequest req) {return null;}
    List<UsedImageDTO> GetUsedImagesByUsedId(ImageRequest.GetUsedImagesByUsedIdRequest req) {return null;}
    void DeleteUsedImage(ImageRequest.DeleteUsedImageRequest req) {}

    //TODO: 검수이미지 추가

}
