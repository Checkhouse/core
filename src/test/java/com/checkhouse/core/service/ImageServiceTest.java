package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.entity.ImageURL;
import com.checkhouse.core.repository.mysql.ImageRepository;
import com.checkhouse.core.request.ImageRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    private ImageURL image1;
    private ImageURL invalidImage;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        image1 = ImageURL.builder()
                .imageId(UUID.randomUUID())
                .imageURL("https://naver.com")
                .build();
        invalidImage = ImageURL.builder()
                .imageId(UUID.randomUUID())
                .imageURL("123456")
                .build();
    }

    @DisplayName("이미지 URL 저장 성공")
    @Test
    void SUCCESS_saveImageUrl() {
        // 이미지 정보
        ImageRequest.AddImageRequest req = new ImageRequest.AddImageRequest(
                image1.getImageId(),
                "https://naver.com"
        );

        // given
        when(imageRepository.save(any())).thenReturn(image1);

        // when
        ImageDTO result = imageService.AddImage(req);

        // then
        assertNotNull(result);
        assertEquals("https://naver.com", result.imageURL());
        verify(imageRepository, times(1)).save(any());
    }

    @DisplayName("이미지 조회 성공")
    @Test
    void SUCCESS_getImage() {
        // 이미지 정보
        UUID imageId = image1.getImageId();
        ImageRequest.GetImageRequest req = new ImageRequest.GetImageRequest(imageId);

        // given
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image1));

        // when
        ImageDTO result = imageService.GetImage(req);

        // then
        assertNotNull(result);
        assertEquals("https://naver.com", result.imageURL());
        verify(imageRepository, times(1)).findById(imageId);
    }
    @DisplayName("이미지 삭제 성공")
    @Test
    void SUCCESS_deleteImage() {
        // 이미지 정보
        UUID imageId = image1.getImageId();

        // given
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image1));

        // when
        ImageRequest.DeleteImageRequest req = new ImageRequest.DeleteImageRequest(imageId);

        imageService.DeleteImage(req);

        // then
        verify(imageRepository, times(1)).findById(imageId);
    }

    @DisplayName("존재하지 않는 이미지 아이디의 경우 조회 실패")
    @Test
    void FAIL_getImage_not_found() {
        //이미지 정보
        UUID invalidId = UUID.randomUUID();
        ImageRequest.GetImageRequest req = new ImageRequest.GetImageRequest(invalidId);
        when(imageRepository.findById(invalidId)).thenReturn(Optional.empty());

        //given, when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.GetImage(req));

        assertEquals(ErrorStatus._IMAGE_ID_NOT_FOUND, exception.getCode());
        verify(imageRepository, times(1)).findById(invalidId);
    }

    @DisplayName("정상적이지 않는 URL의 경우 이미지 URL 저장 실패")
    @Test
    void FAIL_saveImageUrl_invalid_url() {
        //이미지 정보
        ImageURL image = ImageURL.builder()
                .imageId(UUID.randomUUID())
                .imageURL("123456 adsfasdf")
                .build();
        ImageRequest.AddImageRequest req = new ImageRequest.AddImageRequest(image.getImageId(), image.getImageURL());

        //given, when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.AddImage(req));

        assertEquals(ErrorStatus._IMAGE_URL_NOT_EXIST, exception.getCode());
    }
    @DisplayName("존재하지 않는 이미지 아이디의 경우 삭제 실패")
    @Test
    void FAIL_deleteImageUrl_invalid_url() {
        //이미지 정보
        UUID invalidId = UUID.randomUUID();
        ImageRequest.DeleteImageRequest req = new ImageRequest.DeleteImageRequest(invalidId);
        when(imageRepository.findById(invalidId)).thenReturn(Optional.empty());

        //given, when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.DeleteImage(req));

        assertEquals(ErrorStatus._IMAGE_ID_NOT_FOUND, exception.getCode());
        verify(imageRepository, times(1)).findById(invalidId);
    }
}

