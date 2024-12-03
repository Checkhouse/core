package com.checkhouse.core.controller;

import com.checkhouse.core.integration.BaseIntegrationTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.UUID;

import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.controller.TransactionController;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.ImageURL;
import com.checkhouse.core.entity.UsedImage;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.Send;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.TransactionRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.ImageRepository;
import com.checkhouse.core.repository.mysql.UsedImageRepository;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.repository.mysql.SendRepository;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.dto.request.SendRequest;

public class SendIntegrationTest extends BaseIntegrationTest {
	private static String baseUrl = "/api/v1/send";

	@Autowired
	private SendController sendController;
	@Autowired
	private SendRepository sendRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UsedProductRepository usedProductRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private UsedImageRepository usedImageRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OriginProductRepository originProductRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;

	private User savedBuyer;
	private Category savedCategory;
	private OriginProduct savedOriginProduct;
	private User savedSeller;
	private UsedProduct savedCompletedUsedProduct;
	private Transaction savedCompletedTransaction;
	private Address savedAddress;
	private Delivery savedDelivery;
	private Send savedSend;
	private Transaction savedTransaction;
	private UsedProduct savedUsedProduct;

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
        User buyer = User.builder()
				.username("test@email.com")
				.email("test@email.com")
				.nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)   
                .provider("naver")
                .providerId("random id naver")
				.isActive(true)
                .build();
		savedBuyer = userRepository.save(buyer);
		Category category = Category.builder()
			.name("category1")
			.build();
		savedCategory = categoryRepository.save(category);
		OriginProduct originProduct = OriginProduct.builder()
			.name("아이폰")
			.company("애플")
			.category(savedCategory)
			.build();
		savedOriginProduct = originProductRepository.save(originProduct);
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
		// 판매 완료된 상품 (판매 완료 상품 조회용)
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
		// 진행중인 거래 (발송 생성용)
		Transaction transaction = Transaction.builder()
				.buyer(savedBuyer)
				.usedProduct(savedUsedProduct)
				.isCompleted(false)
				.build();
		savedTransaction = transactionRepository.save(transaction);

		// 완료된 거래 (발송 조회용)
		Transaction completedTransaction = Transaction.builder()
			.buyer(savedBuyer)
			.usedProduct(savedCompletedUsedProduct)
			.isCompleted(true)
			.build();
		savedCompletedTransaction = transactionRepository.save(completedTransaction);

		Address address = Address.builder()
                .addressId(UUID.randomUUID())
                .name("홍길동")
                .address("서울특별시 동작구 상도로 369")
				.location(new Point(0, 0))
                .zipcode(6978)
                .phone("01012345678")
                .addressDetail("정보과학관 지하 1층")
                .build();
		savedAddress = addressRepository.save(address);

		Delivery delivery = Delivery.builder()
			.address(savedAddress)
				.deliveryState(DeliveryState.PRE_DELIVERY)
				.trackingCode(null)
			.build();
		savedDelivery = deliveryRepository.save(delivery);

		Send send = Send.builder()
				.transaction(savedCompletedTransaction)
			.state(DeliveryState.PRE_SEND)
			.delivery(savedDelivery)
			.build();
		savedSend = sendRepository.save(send);
	}

	@AfterEach
	void cleanUp() {
		sendRepository.deleteAll();
		deliveryRepository.deleteAll();
		addressRepository.deleteAll();
		transactionRepository.deleteAll();
		usedImageRepository.deleteAll();
		imageRepository.deleteAll();
		usedProductRepository.deleteAll();
		originProductRepository.deleteAll();
		categoryRepository.deleteAll();
		userRepository.deleteAll();
	}

	@DisplayName("발송 등록")
	@Test
	void addSendTest() throws Exception {
		SendRequest.AddSendRequest request = new SendRequest.AddSendRequest(
			savedTransaction.getTransactionId(),
			savedAddress.getAddressId(),
			null
		);
		mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@DisplayName("발송 상태 업데이트")
	@Test
	void updateSendStateTest() throws Exception {
		SendRequest.UpdateSendStateRequest request = new SendRequest.UpdateSendStateRequest(
			savedSend.getSendId(),
			DeliveryState.SENT
		);
		mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@DisplayName("발송 삭제")
	@Test
	void deleteSendTest() throws Exception {
		SendRequest.DeleteSendRequest request = new SendRequest.DeleteSendRequest(
			savedSend.getSendId()
		);
		mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@DisplayName("발송 리스트 조회")
	@Test
	void getSendListTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
			.andExpect(status().isOk());
	}
}