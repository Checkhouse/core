package com.checkhouse.core.controller;
import com.checkhouse.core.repository.mysql.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.data.geo.Point;

import com.checkhouse.core.controller.PickupController;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.entity.Pickup;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.Store;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.dto.request.PickupRequest;

import java.util.UUID;

public class PickupIntegrationTest extends BaseIntegrationTest {
	private static String baseUrl = "/api/v1/pickup";

	@Autowired
	private PickupController pickupController;
	@Autowired
	private PickupRepository pickupRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OriginProductRepository originProductRepository;
	@Autowired
	private UsedProductRepository usedProductRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private StoreRepository storeRepository;
    @Autowired
    private TransactionRepository transactionRepository;

	private User savedSeller;
	private User savedBuyer;
	private Pickup savedPickup;
	private Category savedCategory;
	private Transaction savedTransaction;
	private OriginProduct savedOriginProduct;
	private UsedProduct savedUsedProduct;
	private Store savedStore;
	private Address savedStoreAddress;

	private UsedProduct savedCompletedUsedProduct;
	private Transaction savedCompletedTransaction;

	@BeforeEach
	void setup() {
		User seller = User.builder()
				.username("test user2")
                .email("test2@email.com")
                .nickname("test nickname2")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
				.isActive(true)
                .build();
		savedSeller = userRepository.save(seller);
		// 인증 유저
        User buyer = User.builder()
				.username("test@email.com")
                .email("test@email.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_ADMIN)
                .provider("naver")
                .providerId("random id naver")
				.isActive(true)
                .build();
		savedBuyer = userRepository.save(buyer);

		Category category = Category.builder()
            .name("test category")
            .build();
		savedCategory = categoryRepository.save(category);

        OriginProduct originProduct1 = OriginProduct.builder()
				.id(UUID.randomUUID())
                .name("아이패드")
                .company("애플")
                .category(savedCategory)
                .build();
		savedOriginProduct = originProductRepository.save(originProduct1);

		// 판매 중인 상품 (픽업 생성용)
		UsedProduct usedProduct = UsedProduct.builder()
				.title("아이폰 떨이")
				.description("싸다싸 너만오면 고")
				.price(3000)
				.isNegoAllow(true)
				.state(UsedProductState.ON_SALE)
				.originProduct(savedOriginProduct)
				.user(savedSeller)
				.build();
		savedUsedProduct = usedProductRepository.save(usedProduct);

		// 판매 완료된 상품 (픽업 확인용)
		UsedProduct completedUsedProduct = UsedProduct.builder()
				.title("아이패드 떨이")
				.description("판매완료")
				.price(3000)
				.isNegoAllow(true)
				.state(UsedProductState.PRE_SALE)
				.originProduct(savedOriginProduct)
				.user(savedSeller)
				.build();
		savedCompletedUsedProduct = usedProductRepository.save(completedUsedProduct);

		// 진행중인 거래 (픽업 생성용)
		Transaction transaction = Transaction.builder()
				.buyer(savedBuyer)
				.usedProduct(savedUsedProduct)
				.isCompleted(false)
				.build();
		savedTransaction = transactionRepository.save(transaction);

		// 완료된 거래 (픽업 확인용)
		Transaction completedTransaction = Transaction.builder()
				.buyer(savedBuyer)
				.usedProduct(savedCompletedUsedProduct)
				.isCompleted(true)
				.build();
		savedCompletedTransaction = transactionRepository.save(completedTransaction);

		Address storeaddr = Address.builder()
                .name("홍길동")
                .address("서울특별시 동작구 상도로 369")
                .zipcode(6978)
                .phone("01012345678")
                .addressDetail("정보과학관 지하 1층")
				.location(new Point(127.0323305, 37.5149802))
                .build();
		savedStoreAddress = addressRepository.save(storeaddr);

        Store store = Store.builder()
                .name("정보섬티맥스")
                .code("123456")
                .address(savedStoreAddress)
                .build();
		savedStore = storeRepository.save(store);

		// 완료된 상품, 거래에 대해 생성된 픽업 (픽업 확인용)
		Pickup pickup = Pickup.builder()
			.transaction(savedCompletedTransaction)
			.store(savedStore)
			.isPicked_up(false)
			.build();
		savedPickup = pickupRepository.save(pickup);
	}

	@AfterEach
	void cleanUp() {
		pickupRepository.deleteAll();
		transactionRepository.deleteAll();
		storeRepository.deleteAll();
		usedProductRepository.deleteAll();
		originProductRepository.deleteAll();
		categoryRepository.deleteAll();
		userRepository.deleteAll();
	}

	@DisplayName("픽업 등록")
	@Test
	void addPickupTest() throws Exception {
		PickupRequest.AddPickUpRequest request = new PickupRequest.AddPickUpRequest(
			savedTransaction.getTransactionId(),
			savedStore.getStoreId()
		);

		mockMvc.perform(
			MockMvcRequestBuilders.post(baseUrl)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@DisplayName("사용자 픽업 조회")
	@Test
	void getPickupListTest() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get(baseUrl)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk())
		.andReturn();
	}

	@DisplayName("사용자 픽업 상세 조회")
	@Test
	void getPickupDetailsTest() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get(baseUrl + "/details")
				.contentType(MediaType.APPLICATION_JSON)
				.param("pickupId", savedPickup.getPickupId().toString())
		).andExpect(status().isOk())
		.andReturn();
	}

	@DisplayName("픽업 상태 변경")
	@Test
	void updatePickupTest() throws Exception {
		PickupRequest.UpdatePickUpRequest request = new PickupRequest.UpdatePickUpRequest(
			savedPickup.getPickupId(),
			savedStore.getStoreId(),
			"123456"
		);

		mockMvc.perform(
			MockMvcRequestBuilders.patch(baseUrl)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk())
		.andReturn();
	}

	// 인증 유저(buyer)의 Role 을 admin 으로 수정해야 돌아감
	// 인증 유저가 1명이라 쩔수
	 @DisplayName("관리자 픽업 조회")
	 @Test
	 void getPickupListForAdminTest() throws Exception {
	 	PickupRequest.GetPickUpListForAdminRequest request = new PickupRequest.GetPickUpListForAdminRequest(
	 		savedStore.getStoreId()
	 	);

	 	mockMvc.perform(
	 		MockMvcRequestBuilders.patch(baseUrl + "/admin")
	 			.content(objectMapper.writeValueAsString(request))
	 			.contentType(MediaType.APPLICATION_JSON)
	 	).andExpect(status().isOk())
	 	.andReturn();
	 }
}
