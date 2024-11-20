package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.dto.OriginImageDTO;
import com.checkhouse.core.dto.UsedImageDTO;
import com.checkhouse.core.entity.*;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.repository.mysql.*;
import com.checkhouse.core.dto.request.ImageRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private OriginProductRepository originProductRepository;
    @Mock
    private OriginImageRepository originImageRepository;
    @Mock
    private UsedProductRepository usedProductRepository;
    @Mock
    private UsedImageRepository usedImageRepository;

    @InjectMocks
    private ImageService imageService;

    private User mockedUser;

    private ImageURL image1;
    private ImageURL image2;
    private ImageURL invalidImage;

    private Category category1;

    private OriginProduct originProduct1;

    private OriginImage originImage1;
    private OriginImage originImage2;

    private UsedProduct usedProduct1;

    private UsedImage usedImage1;
    private UsedImage usedImage2;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        mockedUser = User.builder()
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
                .build();

        image1 = ImageURL.builder()
                .imageId(UUID.randomUUID())
                .imageURL("https://naver.com")
                .build();
        image2 = ImageURL.builder()
                .imageId(UUID.randomUUID())
                .imageURL("https://google.com")
                .build();
        invalidImage = ImageURL.builder()
                .imageId(UUID.randomUUID())
                .imageURL("123456")
                .build();

        category1 = Category.builder()
                .name("category1")
                .build();

        originProduct1 = OriginProduct.builder()
                .id(UUID.randomUUID())
                .name("origin product name")
                .company("origin product company")
                .category(category1)
                .build();

        originImage1 = OriginImage.builder()
                .originImageId(UUID.randomUUID())
                .image(image1)
                .originProduct(originProduct1)
                .build();

        originImage2 = OriginImage.builder()
                .originImageId(UUID.randomUUID())
                .image(image2)
                .originProduct(originProduct1)
                .build();

        usedProduct1 = UsedProduct.builder()
                .usedProductId(UUID.randomUUID())
                .originProduct(originProduct1)
                .user(mockedUser)
                .state(UsedProductState.PRE_SALE)
                .title("Used Product Name")
                .description("shit")
                .price(189000)
                .isNegoAllow(false)
                .build();

        usedImage1 = UsedImage.builder()
                .usedImageId(UUID.randomUUID())
                .image(image1)
                .usedProduct(usedProduct1)
                .build();

        usedImage2 = UsedImage.builder()
                .usedImageId(UUID.randomUUID())
                .image(image2)
                .usedProduct(usedProduct1)
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
    //컨트롤러에서는 사용되지 않음 (서비스 내부 로직)
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
        verify(imageRepository, times(1)).delete(image1);
    }

    //원본 이미지
    @DisplayName("원본 이미지 저장 성공")
    @Test
    void SUCCESS_addOriginImage() {
        // 원본 이미지 정보
        ImageRequest.AddOriginImageRequest req = new ImageRequest.AddOriginImageRequest(
                originImage1.getOriginImageId(),
                originProduct1,
                image1.getImageURL()
        );

        // given
        when(imageRepository.save(any())).thenReturn(image1);
        when(originImageRepository.save(any())).thenReturn(originImage1);
        when(originProductRepository.findById(originProduct1.getOriginProductId())).thenReturn(Optional.of(originProduct1));

        // when
        OriginImageDTO result = imageService.AddOriginImage(req);

        // then
        assertNotNull(result);
        assertEquals(result.originProduct().getName(), originProduct1.getName());
        assertEquals(result.image().getImageURL(), image1.getImageURL());
        verify(imageRepository, times(1)).save(any());
        verify(originImageRepository, times(1)).save(any());
        verify(originProductRepository, times(1)).findById(originProduct1.getOriginProductId());
    }
    @DisplayName("원본 이미지 조회 성공")
    @Test
    void SUCCESS_getOriginImage() {
        // 원본 이미지 정보
        ImageRequest.GetOriginImageRequest req = new ImageRequest.GetOriginImageRequest(
                originImage1.getOriginImageId()
        );

        // given
        when(originImageRepository.findById(originImage1.getOriginImageId())).thenReturn(Optional.of(originImage1));

        // when
        OriginImageDTO result = imageService.GetOriginImage(req);

        // then
        assertNotNull(result);
        assertEquals(result.originProduct().getName(), originProduct1.getName());
        assertEquals(result.image().getImageURL(), image1.getImageURL());
        verify(originImageRepository, times(1)).findById(originImage1.getOriginImageId());
    }
    @DisplayName("원본 물품에 대한 이미지 리스트 조회 성공")
    @Test
    void SUCCESS_getOriginImagesByOriginId() {
        // 원본 이미지 정보
        ImageRequest.GetOriginImagesByOriginIdRequest req = new ImageRequest.GetOriginImagesByOriginIdRequest(
                originProduct1.getOriginProductId()
        );

        // given
        when(originProductRepository.findById(originProduct1.getOriginProductId())).thenReturn(Optional.of(originProduct1));
        when(originImageRepository.findUsedImagesByOriginProduct(originProduct1)).thenReturn(List.of(originImage1, originImage2));

        // when
        List<OriginImageDTO> result = imageService.GetOriginImagesByOriginId(req);

        // then
        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(originImageRepository, times(1)).findUsedImagesByOriginProduct(originProduct1);
        verify(originProductRepository, times(1)).findById(originProduct1.getOriginProductId());
    }
    @DisplayName("원본 이미지 삭제 성공")
    @Test
    void SUCCESS_deleteOriginImage() {
        // 원본 이미지 정보
        ImageRequest.DeleteOriginImageRequest req = new ImageRequest.DeleteOriginImageRequest(
                originImage1.getOriginImageId()
        );

        // given
        when(originImageRepository.findById(originImage1.getOriginImageId())).thenReturn(Optional.of(originImage1));

        // when
        imageService.DeleteOriginImage(req);

        // then
        verify(originImageRepository, times(1)).findById(originImage1.getOriginImageId());
        verify(originImageRepository, times(1)).delete(originImage1);
    }

    //중고 이미지
    @DisplayName("중고 이미지 저장 성공")
    @Test
    void SUCCESS_addUsedImage() {
        // 중고 이미지 정보
        ImageRequest.AddUsedImageRequest req = new ImageRequest.AddUsedImageRequest(
                UUID.randomUUID(),
                usedProduct1,
                image1.getImageURL()
        );

        // given
        when(imageRepository.save(any())).thenReturn(image1);
        when(usedImageRepository.save(any())).thenReturn(usedImage1);
        when(usedProductRepository.findById(usedProduct1.getUsedProductId())).thenReturn(Optional.of(usedProduct1));

        // when
        UsedImageDTO result = imageService.AddUsedImage(req);

        // then
        assertNotNull(result);
        assertEquals(result.usedProduct().getTitle(), usedProduct1.getTitle());
        assertEquals(result.usedProduct().getDescription(), usedProduct1.getDescription());
        assertEquals(result.image().getImageURL(), image1.getImageURL());
        verify(imageRepository, times(1)).save(any());
        verify(usedImageRepository, times(1)).save(any());
        verify(usedProductRepository, times(1)).findById(usedProduct1.getUsedProductId());
    }
    @DisplayName("중고 이미지 조회 성공")
    @Test
    void SUCCESS_getUsedImage() {
        // 중고 이미지 정보
        UUID usedImageId = usedImage1.getUsedImageId();
        ImageRequest.GetUsedImageRequest req = new ImageRequest.GetUsedImageRequest(usedImageId);

        // given
        when(usedImageRepository.findById(usedImageId)).thenReturn(Optional.of(usedImage1));

        // when
        UsedImageDTO result = imageService.GetUsedImage(req);

        // then
        assertNotNull(result);
        assertEquals(result.image().getImageURL(), usedImage1.getImage().getImageURL());
        assertEquals(result.usedProduct().getTitle(), usedProduct1.getTitle());
        verify(usedImageRepository, times(1)).findById(usedImageId);
    }
    @DisplayName("중고 물품에 대한 이미지 리스트 조회 성공")
    @Test
    void SUCCESS_getUsedImagesByUsedId() {
        // 중고 이미지 리스트 정보
        UUID usedProductId = usedProduct1.getUsedProductId();
        ImageRequest.GetUsedImagesByUsedIdRequest req = new ImageRequest.GetUsedImagesByUsedIdRequest(usedProductId);

        // given
        when(usedProductRepository.findById(usedProductId)).thenReturn(Optional.of(usedProduct1));
        when(usedImageRepository.findUsedImagesByUsedProduct(usedProduct1)).thenReturn(List.of(usedImage1, usedImage2));

        // when
        List<UsedImageDTO> result = imageService.GetUsedImagesByUsedId(req);

        // then
        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(usedProductRepository, times(1)).findById(usedProductId);
        verify(usedImageRepository, times(1)).findUsedImagesByUsedProduct(usedProduct1);

    }
    @DisplayName("중고 이미지 삭제 성공")
    @Test
    void SUCCESS_deleteUsedImage() {
        // 중고 이미지 정보
        UUID usedImageId = usedImage1.getUsedImageId();
        ImageRequest.DeleteUsedImageRequest req = new ImageRequest.DeleteUsedImageRequest(usedImageId);

        // given
        when(usedImageRepository.findById(usedImageId)).thenReturn(Optional.of(usedImage1));

        // when
        imageService.DeleteUsedImage(req);

        // then
        verify(usedImageRepository, times(1)).findById(usedImageId);
        verify(usedImageRepository, times(1)).delete(usedImage1);
    }


    //TODO: 검수이미지

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

    //원본 이미지
    @DisplayName("원본 상품이 없을 시 저장 실패")
    @Test
    void FAIL_addOriginImageUrl_invalid_origin_product() {
        // 원본 이미지 정보
        ImageRequest.AddOriginImageRequest req = new ImageRequest.AddOriginImageRequest(
                UUID.randomUUID(),
                originProduct1,
                image1.getImageURL()
        );

        // given
        when(originProductRepository.findById(originProduct1.getOriginProductId())).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.AddOriginImage(req));

        assertEquals(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND, exception.getCode());
        verify(originProductRepository, times(1)).findById(originProduct1.getOriginProductId());
    }
    @DisplayName("원본이미지가 존재하지 않으면 조회 실패")
    @Test
    void FAIL_getOriginImageUrl_invalid_Origin_image_id() {
        // 원본 이미지 정보
        UUID originImageId = UUID.randomUUID();
        ImageRequest.GetOriginImageRequest req = new ImageRequest.GetOriginImageRequest(
                originImageId
        );

        // given
        when(originImageRepository.findById(originImageId)).thenReturn(Optional.of(originImage1));

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.GetOriginImage(req));

        assertEquals(ErrorStatus._ORIGIN_IMAGE_ID_NOT_FOUND, exception.getCode());
        verify(originImageRepository, times(1)).findById(originImageId);
    }
    @DisplayName("원본 상품이 존재하지 않으면 리스트 조회 실패")
    @Test
    void FAIL_getOriginImageByOriginId_invalid_Origin_image_id() {
        // 원본 이미지 정보
        UUID originImageId = UUID.randomUUID();
        ImageRequest.GetOriginImagesByOriginIdRequest req = new ImageRequest.GetOriginImagesByOriginIdRequest(
                originImageId
        );

        // given
        when(originProductRepository.findById(originProduct1.getOriginProductId())).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.GetOriginImagesByOriginId(req));

        assertEquals(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND, exception.getCode());
        verify(originProductRepository, times(1)).findById(originProduct1.getOriginProductId());
    }
    @DisplayName("원본이미지가 존재하지 않으면 삭제 실패")
    @Test
    void Fail_deleteOriginImageUrl_invalid_Origin_image_id() {
        // 원본 이미지 정보
        UUID originImageId = UUID.randomUUID();
        ImageRequest.DeleteImageRequest req = new ImageRequest.DeleteImageRequest(
                originImageId
        );

        // given
        when(originImageRepository.findById(originImageId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.DeleteImage(req));

        assertEquals(ErrorStatus._ORIGIN_IMAGE_ID_NOT_FOUND, exception.getCode());
        verify(originImageRepository, times(1)).findById(originImageId);
    }

    //중고 이미지
    @DisplayName("중고 상품이 없을 시 저장 실패")
    @Test
    void FAIL_addUsedImageUrl_invalid_origin_product() {
        // 중고 이미지 정보
        ImageRequest.AddUsedImageRequest req = new ImageRequest.AddUsedImageRequest(
                UUID.randomUUID(),
                usedProduct1,
                image1.getImageURL()
        );

        // given
        when(usedProductRepository.findById(usedProduct1.getUsedProductId())).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.AddUsedImage(req));

        assertEquals(ErrorStatus._USED_PRODUCT_NOT_FOUND, exception.getCode());
        verify(usedProductRepository, times(1)).findById(usedProduct1.getUsedProductId());
    }
    @DisplayName("중고이미지가 존재하지 않으면 조회 실패")
    @Test
    void FAIL_getUsedImageUrl_invalid_Origin_image_id() {
        // 중고 이미지 정보
        UUID usedImageId = UUID.randomUUID();
        ImageRequest.GetUsedImageRequest req = new ImageRequest.GetUsedImageRequest(usedImageId);

        // given
        when(usedImageRepository.findById(usedImageId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.GetUsedImage(req));

        assertEquals(ErrorStatus._USED_IMAGE_ID_NOT_FOUND, exception.getCode());
        verify(usedImageRepository, times(1)).findById(usedImageId);
    }
    @DisplayName("중고 상품이 존재하지 않으면 리스트 조회 실패")
    @Test
    void FAIL_getUsedImageByUsedId_invalid_Used_image_id() {
        // 중고 이미지 정보
        UUID usedProductId = UUID.randomUUID();
        ImageRequest.GetUsedImagesByUsedIdRequest req = new ImageRequest.GetUsedImagesByUsedIdRequest(usedProductId);

        // given
        when(usedProductRepository.findById(usedProductId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.GetUsedImagesByUsedId(req));

        assertEquals(ErrorStatus._USED_PRODUCT_NOT_FOUND, exception.getCode());
        verify(usedProductRepository, times(1)).findById(usedProductId);
    }
    @DisplayName("중고이미지가 존재하지 않으면 삭제 실패")
    @Test
    void Fail_deleteUsedImageUrl_invalid_Origin_image_id() {
        // 중고 이미지 정보
        UUID usedImageId = UUID.randomUUID();
        ImageRequest.DeleteUsedImageRequest req = new ImageRequest.DeleteUsedImageRequest(usedImageId);

        // given
        when(usedImageRepository.findById(usedImageId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> imageService.DeleteUsedImage(req));

        assertEquals(ErrorStatus._USED_IMAGE_ID_NOT_FOUND, exception.getCode());
        verify(usedImageRepository, times(1)).findById(usedImageId);
    }

    //TODO: 검수이미지
}

