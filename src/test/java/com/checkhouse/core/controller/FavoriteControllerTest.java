package com.checkhouse.core.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.checkhouse.core.dto.request.FavoriteRequest;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.UserAddress;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.repository.mysql.FavoriteOriginRepository;
import com.checkhouse.core.repository.mysql.FavoriteUsedRepository;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.UserAddressRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.checkhouse.core.service.FavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.checkhouse.core.entity.enums.UsedProductState.*;

public class FavoriteControllerTest extends BaseIntegrationTest {
    private static final String BASE_URL = "/api/v1/favorite";

    @Autowired
    private UsedProductRepository usedProductRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OriginProductRepository originProductRepository;

    private Category savedCategory;
    private OriginProduct savedOriginProduct;

    @Autowired
    private AddressRepository addressRepository;
    private Address savedAddress;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private UsedProduct savedUsedProduct;

    @Autowired
    private UserAddressRepository userAddressRepository;

    private UserAddress savedUserAddress;

    @Autowired
    private HubRepository hubRepository;
    private Hub savedHub;

    @Autowired
    private FavoriteOriginRepository favoriteOriginRepository;

    @Autowired
    private FavoriteUsedRepository favoriteUsedRepository;

    

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
        savedUser = userRepository.saveAndFlush(user);

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
            .state(ON_SALE)
            .build();
        savedUsedProduct = usedProductRepository.saveAndFlush(usedProduct);
    }

    @AfterEach
    void tearDown() {
        favoriteOriginRepository.deleteAll();
        favoriteUsedRepository.deleteAll();
        usedProductRepository.deleteAll();
        userAddressRepository.deleteAll();
        hubRepository.deleteAll();
        addressRepository.deleteAll();
        originProductRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    //origin favorite 등록 성공
   @Test
   @DisplayName("origin favorite 등록 성공")
   void addFavoriteOriginSuccess() throws Exception {
    FavoriteRequest.AddToFavoriteRequest request = FavoriteRequest.AddToFavoriteRequest.builder()
            .originProductId(savedOriginProduct.getOriginProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL + "/origin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
   }

   //used favorite 등록 성공
   @Test
   @DisplayName("used favorite 등록 성공")
   void addFavoriteUsedSuccess() throws Exception {
    FavoriteRequest.AddUsedProductLikeRequest request = FavoriteRequest.AddUsedProductLikeRequest.builder()
            .usedProductId(savedUsedProduct.getUsedProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL + "/used")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
   }

   //origin favorite 삭제 성공
   @Test
   @DisplayName("origin favorite 삭제 성공")
   void removeFavoriteOriginSuccess() throws Exception {
        // 먼저 favorite 생성
        FavoriteRequest.AddToFavoriteRequest createRequest = FavoriteRequest.AddToFavoriteRequest.builder()
            .originProductId(savedOriginProduct.getOriginProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL + "/origin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        ).andExpect(status().isOk());

        // 그 다음 삭제 요청
        FavoriteRequest.RemoveFromFavoriteRequest request = FavoriteRequest.RemoveFromFavoriteRequest.builder()
            .originProductId(savedOriginProduct.getOriginProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.delete(BASE_URL + "/origin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
   }

   //used favorite 삭제 성공
   @Test
   @DisplayName("used favorite 삭제 성공")
   void removeFavoriteUsedSuccess() throws Exception {
        // 먼저 favorite 생성
        FavoriteRequest.AddUsedProductLikeRequest createRequest = FavoriteRequest.AddUsedProductLikeRequest.builder()
            .usedProductId(savedUsedProduct.getUsedProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL + "/used")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        ).andExpect(status().isOk());

        // 그 다음 삭제 요청
        FavoriteRequest.RemoveUsedProductLikeRequest request = FavoriteRequest.RemoveUsedProductLikeRequest.builder()
            .usedProductId(savedUsedProduct.getUsedProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.delete(BASE_URL + "/used")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
   }

   //origin favorite 조회 성공
   @Test
   @DisplayName("origin favorite 조회 성공")
   void getUserFavoriteOriginsSuccess() throws Exception {
        // 먼저 favorite 생성
        FavoriteRequest.AddToFavoriteRequest createRequest = FavoriteRequest.AddToFavoriteRequest.builder()
            .originProductId(savedOriginProduct.getOriginProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL + "/origin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        ).andExpect(status().isOk());

        // 그 다음 조회
        mockMvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + "/origin")
                .param("userId", savedUser.getUserId().toString())
        ).andExpect(status().isOk());
   }

   //used favorite 조회 성공
   @Test
   @DisplayName("used favorite 조회 성공")
   void getUserFavoriteUsedSuccess() throws Exception {
    FavoriteRequest.GetUserFavoriteUsed request = FavoriteRequest.GetUserFavoriteUsed.builder()
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + "/used")
                .param("userId", savedUser.getUserId().toString())
        ).andExpect(status().isOk());
   }

   // origin favorite 개수 조회 성공
   @Test
   @DisplayName("origin favorite 개수 조회 성공")
   void getOriginProductFavoriteCountSuccess() throws Exception {
        // 먼저 favorite 생성
        FavoriteRequest.AddToFavoriteRequest createRequest = FavoriteRequest.AddToFavoriteRequest.builder()
            .originProductId(savedOriginProduct.getOriginProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL + "/origin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        ).andExpect(status().isOk());

        // 그 다음 개수 조회
        mockMvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + "/origin/count")
                .param("originProductId", savedOriginProduct.getOriginProductId().toString())
        ).andExpect(status().isOk());
   }

   //used favorite 개수 조회 성공
   @Test
   @DisplayName("used favorite 개수 조회 성공")
   void getUsedProductFavoriteCountSuccess() throws Exception {
        // 먼저 favorite 생성
        FavoriteRequest.AddUsedProductLikeRequest createRequest = FavoriteRequest.AddUsedProductLikeRequest.builder()
            .usedProductId(savedUsedProduct.getUsedProductId())
            .userId(savedUser.getUserId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL + "/used")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        ).andExpect(status().isOk());

        // 그 다음 개수 조회
        mockMvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + "/used/count")
                .param("usedId", savedUsedProduct.getUsedProductId().toString())
        ).andExpect(status().isOk());
   }
}
