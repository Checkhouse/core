package com.checkhouse.core.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.checkhouse.core.dto.request.UsedProductRequest;
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
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.UserAddressRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MockMvc;
import static com.checkhouse.core.entity.enums.UsedProductState.ON_SALE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.checkhouse.core.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;


class UsedProductControllerTest extends BaseIntegrationTest {

    private static final String baseUrl = "/api/v1/used-products";

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
            .zipcode(12345)
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
    void cleanUp() {
        usedProductRepository.deleteAll();
        userAddressRepository.deleteAll();
        hubRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
        originProductRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("중고 상품 등록 성공")
    void addUsedProduct_success() throws Exception {
        List<String> imgs = new ArrayList<>();
        imgs.add(        "https://www.notateslaapp.com/images/news/2023/tesla-crash-4.jpg");
        imgs.add(        "https://www.notateslaapp.com/images/news/2023/tesla-crash-5.jpg");
        UsedProductRequest.AddUsedProductRequest request = UsedProductRequest.AddUsedProductRequest.builder()
            .title("테스트 중고 상품")
            .description("테스트 중고 상품 설명")
            .price(10000)
            .isNegoAllow(true)
            .originProductId(savedOriginProduct.getOriginProductId())
            .userId(savedUser.getUserId())
                .userAddressId(savedUserAddress.getUserAddressId())
                .usedImageList(imgs)
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("중고 상품 정보 수정 성공")
    void updateUsedProductInfoSuccess() throws Exception {
        UsedProductRequest.UpdateUsedProductInfo request = UsedProductRequest.UpdateUsedProductInfo.builder()
            .usedProductId(savedUsedProduct.getUsedProductId())
            .title("수정된 중고 상품")
            .description("수정된 설명")
            .price(20000)
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.patch(baseUrl + "/" + savedUsedProduct.getUsedProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("중고 상품 정보 조회 성공")
    void getUsedProductSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(baseUrl)
                .param("usedProductId", savedUsedProduct.getUsedProductId().toString())
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("중고 상품 상태별 목록 조회 성공")
    void getUsedProductsByStatusSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(baseUrl + "/list")
                .param("status", ON_SALE.name())
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자별 중고 상품 목록 조회 성공")
    void getUsedProductsByUserSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(baseUrl + "/list/user?userId=" + savedUser.getUserId())
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("원본 상품별 중고 상품 목록 조회 성공")
    void getUsedProductWithOriginIdSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(baseUrl + "/origin/" + savedOriginProduct.getOriginProductId())
                .param("originProductId", savedOriginProduct.getOriginProductId().toString())
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("중고 상품 삭제 성공")
    void deleteUsedProductSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(baseUrl + "/" + savedUsedProduct.getUsedProductId())
        ).andExpect(status().isOk());
    }
}
