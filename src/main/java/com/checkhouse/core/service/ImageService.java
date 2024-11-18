package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.repository.mysql.ImageRepository;
import com.checkhouse.core.request.ImageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    void checkURL(String URL) {}

    ImageDTO AddImage(ImageRequest.AddImageRequest req) {return null;}
    ImageDTO GetImage(ImageRequest.GetImageRequest req) {return null;}
    ImageDTO DeleteImage(ImageRequest.DeleteImageRequest req) {return null;}

    //TODO: 원본이미지, 중고이미지, 검수이미지 추가

}
