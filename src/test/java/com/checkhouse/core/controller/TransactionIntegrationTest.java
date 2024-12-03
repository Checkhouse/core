package com.checkhouse.core.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.checkhouse.core.repository.mysql.TransactionRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.ImageRepository;
import com.checkhouse.core.repository.mysql.UsedImageRepository;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;

import com.checkhouse.core.dto.request.TransactionRequest;

public class TransactionIntegrationTest extends BaseIntegrationTest {
	private static String baseUrl = "/api/v1/transaction";

	@Autowired
	private TransactionController transactionController;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UsedProductRepository usedProductRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OriginProductRepository originProductRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private UsedImageRepository usedImageRepository;

	private Transaction savedTransaction;
	private User savedBuyer;
	private UsedProduct savedUsedProduct;
	private ImageURL savedImageURL;
	private UsedImage savedUsedImage;
	private Category savedCategory;
	private OriginProduct savedOriginProduct;
	private User savedSeller;
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

        ImageURL imageURL = ImageURL.builder()
                .imageURL("https://ssu.ac.kr/wp-content/uploads/2024/07/%EC%A3%BC%EC%9A%94%EB%89%B4%EC%8A%A402.jpg")
                .build();
		savedImageURL = imageRepository.save(imageURL);
		
		UsedImage usedImage = UsedImage.builder()
			.image(savedImageURL)
			.usedProduct(savedCompletedUsedProduct)
			.build();
		savedUsedImage = usedImageRepository.save(usedImage);

		// 진행중인 거래
		Transaction transaction = Transaction.builder()
				.buyer(savedBuyer)
				.usedProduct(savedUsedProduct)
				.isCompleted(false)
				.build();
		savedTransaction = transactionRepository.save(transaction);

		// 완료된 거래 (완료 거래 조회용)
		Transaction completedTransaction = Transaction.builder()
			.buyer(savedBuyer)
			.usedProduct(savedCompletedUsedProduct)
			.isCompleted(true)
			.build();
		savedCompletedTransaction = transactionRepository.save(completedTransaction);
	}

	@AfterEach
	void cleanup() {
		transactionRepository.deleteAll();
		usedImageRepository.deleteAll();
		imageRepository.deleteAll();
		usedProductRepository.deleteAll();
		originProductRepository.deleteAll();
		categoryRepository.deleteAll();
		userRepository.deleteAll();
	}

//	@Test
//	@DisplayName("거래 생성 성공")
//	void SUCCESS_createTransaction() throws Exception {
//		TransactionRequest.AddTransactionRequest request = TransactionRequest.AddTransactionRequest.builder()
//			.usedProductId(savedUsedProduct.getUsedProductId())
//			.buyerId(savedBuyer.getUserId())
//			.build();
//
//		mockMvc.perform(
//				MockMvcRequestBuilders.post(baseUrl)
//				.content(objectMapper.writeValueAsString(request))
//				.contentType(MediaType.APPLICATION_JSON)
//		).andExpect(status().isOk());
//	}

	@Test
	@DisplayName("거래 조회 성공")
	void SUCCESS_getTransaction() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get(baseUrl + "?transactionId=" + savedTransaction.getTransactionId())
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("거래 상태 변경 성공")
	void SUCCESS_updateTransaction() throws Exception {
		TransactionRequest.UpdateTransactionRequest request = TransactionRequest.UpdateTransactionRequest.builder()
			.transactionId(savedTransaction.getTransactionId())
			.build();

		mockMvc.perform(
			MockMvcRequestBuilders.patch(baseUrl)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("구매한 상품 조회 성공")
	void SUCCESS_getPurchasedProducts() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get(baseUrl + "/purchased?userId=" + savedBuyer.getUserId())
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}
}
