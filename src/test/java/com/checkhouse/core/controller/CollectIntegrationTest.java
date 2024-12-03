package com.checkhouse.core.controller;

import com.checkhouse.core.entity.enums.UsedProductState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.UUID;
import org.springframework.data.geo.Point;

import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.dto.request.CollectRequest;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.entity.UserAddress;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.Collect;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.repository.mysql.UserAddressRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.repository.mysql.CollectRepository;

public class CollectIntegrationTest extends BaseIntegrationTest {
	private static String baseUrl = "/api/v1/collect";

	@Autowired
	private CollectController collectController;
	@Autowired
	private CollectRepository collectRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;
	@Autowired
	private UsedProductRepository usedProductRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private HubRepository hubRepository;
	@Autowired
	private UserAddressRepository userAddressRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OriginProductRepository originProductRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	private Address savedAddress;
	private Hub savedHub;
	private UserAddress savedUserAddress;
	private User savedUser;
	private OriginProduct savedOriginProduct;
	private Category savedCategory;
	private UsedProduct savedUsedProduct;
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
            .state(UsedProductState.PRE_SALE)
            .build();
        savedUsedProduct = usedProductRepository.saveAndFlush(usedProduct);

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
	void cleanUp() {
		collectRepository.deleteAll();
		deliveryRepository.deleteAll();
		usedProductRepository.deleteAll();
		userAddressRepository.deleteAll();
		hubRepository.deleteAll();
		addressRepository.deleteAll();
		userRepository.deleteAll();
		originProductRepository.deleteAll();
		categoryRepository.deleteAll();
	}

	@Test
	@DisplayName("수거 상태 업데이트")
	void updateCollectStateTest() throws Exception {
		CollectRequest.UpdateCollectRequest req = new CollectRequest.UpdateCollectRequest(
			savedCollect.getCollectId(),
			DeliveryState.COLLECTED
        );

		mockMvc.perform(
			MockMvcRequestBuilders.patch(baseUrl)
                    .content(objectMapper.writeValueAsString(req))
				    .contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("수거 리스트 조회")
	void getCollectListTest() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get(baseUrl)
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("수거 삭제")
	void deleteCollectTest() throws Exception {
		CollectRequest.DeleteCollectRequest req = new CollectRequest.DeleteCollectRequest(
			savedCollect.getCollectId()
		);

		mockMvc.perform(
			MockMvcRequestBuilders.delete(baseUrl)
				.content(objectMapper.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("수거 상태 조회")
	void getCollectStateTest() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get(baseUrl + "/state")
				.param("collectId", savedCollect.getCollectId().toString())
		).andExpect(status().isOk());
	}
}
