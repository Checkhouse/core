package com.checkhouse.core.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.checkhouse.core.dto.request.StoreRequest;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.Store;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.UserAddress;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.repository.mysql.StoreRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.UserAddressRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.checkhouse.core.entity.enums.UsedProductState.ON_SALE;

public class StoreControllerTest extends BaseIntegrationTest {
    private static final String BASE_URL = "/api/v1/store";

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

    private Store savedStore;

    @Autowired
    private StoreRepository storeRepository;

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

        Store store = Store.builder()
            .name("테스트 스토어")
            .code("test code")
            .address(savedAddress)
            .build();
        savedStore = storeRepository.saveAndFlush(store);

    }

    @AfterEach
    void cleanUp() {
        storeRepository.deleteAll();
        
        usedProductRepository.deleteAll();
        
        userAddressRepository.deleteAll();
        hubRepository.deleteAll();
        addressRepository.deleteAll();
        
        originProductRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("스토어 추가 성공")
    void addStore_success() throws Exception {
        Address newAddress = Address.builder()
            .address("서울시 강남구 역삼동 2")
            .addressDetail("789번지 101호")
            .location(new Point(235, 235))
            .zipcode(12346)
            .phone("010-9876-5432")
            .name("test user 2")
            .build();
        Address savedNewAddress = addressRepository.saveAndFlush(newAddress);

        StoreRequest.AddStoreRequest request = StoreRequest.AddStoreRequest.builder()
            .name("새로운 테스트 스토어")
            .code("test code")
            .addressId(savedNewAddress.getAddressId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("스토어 조회 성공")
    void getStore_success() throws Exception {
        StoreRequest.GetStoreRequest request = StoreRequest.GetStoreRequest.builder()
            .storeId(savedStore.getStoreId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.get(BASE_URL)
                .param("storeId", savedStore.getStoreId().toString())
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("스토어 리스트 조회 성공")
    void getStores_success() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + "/list")
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("스토어 정보 수정 성공")
    void updateStore_success() throws Exception {
        StoreRequest.UpdateStoreRequest request = StoreRequest.UpdateStoreRequest.builder()
            .storeId(savedStore.getStoreId())
            .addressId(savedAddress.getAddressId())
            .name("테스트 스토어 수정")
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.patch(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("스토어 코드 수정 성공")
    void updateStoreCode_success() throws Exception {
        StoreRequest.UpdateStoreCodeRequest request = StoreRequest.UpdateStoreCodeRequest.builder()
            .storeId(savedStore.getStoreId())
            .code("test code 수정")
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.patch(BASE_URL + "/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("스토어 삭제 성공")
    void deleteStore_success() throws Exception {
        StoreRequest.DeleteStoreRequest request = StoreRequest.DeleteStoreRequest.builder()
            .storeId(savedStore.getStoreId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.delete(BASE_URL)
                .param("storeId", savedStore.getStoreId().toString())
        ).andExpect(status().isOk());
    }
}
