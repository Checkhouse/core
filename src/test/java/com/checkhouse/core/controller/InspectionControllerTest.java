package com.checkhouse.core.controller;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import com.checkhouse.core.entity.*;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.*;
import com.checkhouse.core.service.UsedProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.checkhouse.core.dto.request.InspectionRequest;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.service.InspectionService;

public class InspectionControllerTest extends BaseIntegrationTest {

    private static final String baseUrl = "/api/v1/inspection";

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private UsedProductRepository usedProductRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OriginProductRepository originProductRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private HubRepository hubRepository;
    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private UsedImageRepository usedImageRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private CollectRepository collectRepository;

    private UsedProduct savedUsedProduct;
    private User savedUser;
    private OriginProduct savedOriginProduct;
    private Inspection savedInspection;
    private Category savedCategory;
    private Address savedAddress;
    private Hub savedHub;
    private UserAddress savedUserAddress;
    private Delivery savedDelivery;
    private Collect savedCollect;


    @BeforeEach
    void setup() {
        User user = User.builder()
                .username("test@email.com")
                .email("test@email.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
                .isActive(true)
                .build();
        savedUser = userRepository.save(user);
        
        Category category = Category.builder()
            .name("태블릿")
            .build();
		savedCategory = categoryRepository.saveAndFlush(category);

        OriginProduct originProduct1 = OriginProduct.builder()
                .name("아이패드")
                .company("애플")
                .category(savedCategory)
                .build();
        savedOriginProduct = originProductRepository.saveAndFlush(originProduct1);

        Address address = Address.builder()
            .address("서울시 강남구 역삼동")
            .addressDetail("123번지 456호")
            .location(new Point(234, 234))
            .zipcode("12345")
            .phone("010-1234-5678")
            .name("test user")
            .build();
        savedAddress = addressRepository.saveAndFlush(address);

        Hub hub = Hub.builder()
            .name("테스트 허브")
            .address(savedAddress)
            .build();
        savedHub = hubRepository.saveAndFlush(hub);

        UserAddress userAddress = UserAddress.builder()
            .address(savedAddress)
            .user(savedUser)
            .hub(savedHub)
            .build();
        savedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        UsedProduct usedProduct = UsedProduct.builder()
            .user(savedUser)
            .originProduct(savedOriginProduct)
            .title("테스트 중고 상품")
            .description("테스트 중고 상품 설명")
            .price(10000)
            .isNegoAllow(true)
            .state(UsedProductState.ON_SALE)
            .build();
        savedUsedProduct = usedProductRepository.save(usedProduct);

        Delivery delivery = Delivery.builder()
                .address(savedAddress)
                .trackingCode(null)
                .deliveryState(DeliveryState.PRE_DELIVERY)
                .build();
        savedDelivery = deliveryRepository.saveAndFlush(delivery);

        Collect collect = Collect.builder()
                .usedProduct(savedUsedProduct)
                .delivery(savedDelivery)
                .state(DeliveryState.PRE_COLLECT)
                .build();
        savedCollect = collectRepository.saveAndFlush(collect);
    }

    @AfterEach
    void cleanup() {
        collectRepository.deleteAll();
        deliveryRepository.deleteAll();

        // 검수 이미지 먼저 삭제 (만약 있다면)
        inspectionRepository.deleteAll();
        
        // 중고 상품 관련
        usedProductRepository.deleteAll();
        
        // 주소 관련 (외래 키 순서 고려)
        userAddressRepository.deleteAll();
        hubRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
        originProductRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    // 검수 생성이 필요한 테스트를 위한 헬퍼 메소드
    private void createInspection() {
        Inspection inspection = Inspection.builder()
            .usedProduct(savedUsedProduct)
            .description("테스트 검수 설명")
            .isDone(false)
            .user(savedUser)
            .build();
        savedInspection = inspectionRepository.save(inspection);
    }

    //검수 등록 성공
    @Test
    @DisplayName("검수 등록 성공")
    void addInspectionSuccess() throws Exception {
        InspectionRequest.AddInspectionRequest request = InspectionRequest.AddInspectionRequest.builder()
            .usedProductId(savedUsedProduct.getUsedProductId())
            .userId(savedUser.getUserId())
            .description("테스트 검수 설명")
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl + "/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk());
    }
    
    // 검수 사진 업데이트, 검수 노트 업데이트, 검수 상태 업데이트
    @Test
    @DisplayName("검수 사진 업데이트, 검수 노트 업데이트, 검수 상태 업데이트")
    void updateInspectionSuccess() throws Exception {
        createInspection();  // 검수 생성

        // 중고 이미지 먼저 생성
        ImageURL imageUrl1 = imageRepository.save(
            ImageURL.builder()
                .imageURL("https://test.com/image1.jpg")
                .build()
        );
        ImageURL imageUrl2 = imageRepository.save(
            ImageURL.builder()
                .imageURL("https://test.com/image2.jpg")
                .build()
        );

        UsedImage usedImage1 = usedImageRepository.save(
            UsedImage.builder()
                .usedProduct(savedUsedProduct)
                .image(imageUrl1)
                .build()
        );
        UsedImage usedImage2 = usedImageRepository.save(
            UsedImage.builder()
                .usedProduct(savedUsedProduct)
                .image(imageUrl2)
                .build()
        );

        InspectionRequest.UpdateInspectionRequest request = InspectionRequest.UpdateInspectionRequest.builder()
            .inspectionId(savedInspection.getInspectionId())
            .imageURL(List.of("https://test.com/image1.jpg", "https://test.com/image2.jpg"))
            .usedImageId(List.of(usedImage1.getUsedImageId(), usedImage2.getUsedImageId()))  // 실제 존재하는 ID 사용
            .description("테스트 검수 설명 업데이트")
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl + "/finish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result.description").value("테스트 검수 설명 업데이트"))
        .andExpect(jsonPath("$.result.isDone").value(true));
    }
    
   //검수 리스트 조회 성공
   @Test
   @DisplayName("검수 리스트 조회 성공")
   void getInspectionListSuccess() throws Exception {
        createInspection();  // 이 테스트에서도 검수가 필요하므로 여기서 생성
        mockMvc.perform(
            MockMvcRequestBuilders.get(baseUrl + "/list")
                .param("usedProductId", savedUsedProduct.getUsedProductId().toString())
        ).andExpect(status().isOk());
   }

   // 검수 삭제 성공
   @Test
   @DisplayName("검수 삭제 성공")
   void deleteInspectionSuccess() throws Exception {
        createInspection();

        mockMvc.perform(
            MockMvcRequestBuilders.delete(baseUrl + "/" + savedInspection.getInspectionId())
        ).andExpect(status().isOk());
   }
   
}
