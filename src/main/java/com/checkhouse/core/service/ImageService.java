package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.entity.ImageURL;
import com.checkhouse.core.repository.mysql.ImageRepository;
import com.checkhouse.core.dto.request.ImageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

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

    //TODO: 원본이미지, 중고이미지, 검수이미지 추가

}
