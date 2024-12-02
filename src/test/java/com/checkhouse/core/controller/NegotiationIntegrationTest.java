package com.checkhouse.core.controller;

import com.checkhouse.core.entity.*;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.repository.mysql.NegotiationRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.entity.enums.NegotiationState;
import com.checkhouse.core.dto.request.NegotiationRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

public class NegotiationIntegrationTest extends BaseIntegrationTest {
	private static String baseUrl = "/api/v1/negotiation";

	@Autowired
	private NegotiationController negotiationController;

	@Autowired
	private NegotiationRepository negotiationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private OriginProductRepository originProductRepository;

	@Autowired
	private UsedProductRepository usedProductRepository;

	private User savedSeller;
	private User savedBuyer;
	private Category savedCategory;
	private OriginProduct savedOriginProduct;
	private UsedProduct savedUsedProduct;
	private Negotiation savedNegotiation;

	@BeforeEach
	void setup() {
		User seller = User.builder()
                .username("test@email.com")
                .email("test@email.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
				.isActive(true)
                .build();
		savedSeller = userRepository.save(seller);
        User buyer = User.builder()
                .username("test user2")
                .email("test2@email.com")
                .nickname("test nickname2")
                .password(null)
                .role(Role.ROLE_USER)   
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

//		originProductRepository.hardDeleteByNameIfSoftDeleted(originProduct1.getName());
		savedOriginProduct = originProductRepository.save(originProduct1);

		UsedProduct usedProduct1 = UsedProduct.builder()
                .title("아이패드 떨이")
                .description("싸다싸 너만오면 고")
                .price(3000)
                .isNegoAllow(true)
                .state(UsedProductState.ON_SALE)
                .originProduct(savedOriginProduct)
                .user(seller)
                .build();
		savedUsedProduct = usedProductRepository.save(usedProduct1);

        Negotiation negotiation1 = Negotiation.builder()
                .price(2000)
                .state(NegotiationState.WAITING)
                .usedProduct(savedUsedProduct)
                .seller(seller)
                .buyer(buyer)
                .build();
		 savedNegotiation = negotiationRepository.save(negotiation1);
	}

	@AfterEach
	void cleanUp() {
		negotiationRepository.deleteAll();
		usedProductRepository.deleteAll(); // UsedProduct 정리
		originProductRepository.deleteAll(); // OriginProduct 정리
		categoryRepository.deleteAll(); // Category 정리
		userRepository.deleteAll();
	}

	@DisplayName("네고 등록")
	@Test
	void addNegotiationTest() throws Exception {
		NegotiationRequest.AddNegotiationRequest request = NegotiationRequest.AddNegotiationRequest.builder()
			.usedProductId(savedUsedProduct.getUsedProductId())
			.sellerId(savedSeller.getUserId())
			.buyerId(savedBuyer.getUserId())
			.price(2500)
			.build();

		mockMvc.perform(
			MockMvcRequestBuilders.post(baseUrl)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@DisplayName("네고 상태변경/승인")
	@Test
	void approveNegotiationTest() throws Exception {
		NegotiationRequest.UpdateNegotiationRequest request = new NegotiationRequest.UpdateNegotiationRequest(
			savedNegotiation.getNegotiationId(),
			NegotiationState.ACCEPTED
		);
		mockMvc.perform(
			MockMvcRequestBuilders.patch(baseUrl)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@DisplayName("보낸 네고 조회")
	@Test
	void getNegotiationByBuyerTest() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get(baseUrl + "/buy")
		).andExpect(status().isOk())
				.andReturn();
	}

	@DisplayName("받은 네고 조회")
	@Test
	void getNegotiationBySellerTest() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get(baseUrl + "/sell")
		).andExpect(status().isOk())
				.andReturn();
	}

}