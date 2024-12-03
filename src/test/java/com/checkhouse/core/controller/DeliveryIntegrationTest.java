package com.checkhouse.core.controller;

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
import com.checkhouse.core.dto.request.DeliveryRequest;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;

public class DeliveryIntegrationTest extends BaseIntegrationTest {
	private static String baseUrl = "/api/v1/delivery";

	@Autowired
	private DeliveryController deliveryController;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;

	private Address savedDel1addr;
	private Address savedDel2addr;
	private Delivery savedDel1;
	private Delivery savedDel2;

	@BeforeEach
	void setup() {
		Address del1addr = Address.builder()
				.address("서울시 강남구 역삼동")
				.addressDetail("123번지 456호")
				.location(new Point(234, 234))
				.zipcode(12345)
				.phone("010-1234-5678")
				.name("test user")
				.build();
		savedDel1addr = addressRepository.save(del1addr);
    
        Address del2addr = Address.builder()
				.addressId(UUID.randomUUID())
				.name("홍길동")
				.address("서울특별시 동작구 상도로 369")
				.location(new Point(0, 0))
				.zipcode(6978)
				.phone("01012345678")
				.addressDetail("정보과학관 지하 1층")
				.build();
		savedDel2addr = addressRepository.save(del2addr);

        Delivery del1 = Delivery.builder()
            .trackingCode(null)
            .deliveryState(DeliveryState.PRE_DELIVERY)
            .address(savedDel1addr)
            .build();
		savedDel1 = deliveryRepository.save(del1);

        Delivery del2 = Delivery.builder()
            .trackingCode("1234567890")
            .deliveryState(DeliveryState.DELIVERING)
            .address(savedDel2addr)
            .build();
		savedDel2 = deliveryRepository.save(del2);
	}

	@AfterEach
	void cleanup() {
		deliveryRepository.deleteAll();
		addressRepository.deleteAll();
	}

	@DisplayName("배송 등록")
	@Test
	void addDeliveryTest() throws Exception {
		DeliveryRequest.AddDeliveryRequest request = new DeliveryRequest.AddDeliveryRequest(
			savedDel1addr.getAddressId(),
			null
		);
		mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@DisplayName("배송 상태 업데이트")
	@Test
	void updateDeliveryStateTest() throws Exception {
		DeliveryRequest.UpdateDeliveryStateRequest request = new DeliveryRequest.UpdateDeliveryStateRequest(
			savedDel1.getDeliveryId(),
			DeliveryState.DELIVERING
		);
		mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@DisplayName("송장 번호 등록")
	@Test
	void registerTrackingCodeTest() throws Exception {
		DeliveryRequest.RegisterTrackingCodeRequest request = new DeliveryRequest.RegisterTrackingCodeRequest(
			savedDel1.getDeliveryId(),
			"1234567890"
		);
		mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/tracking-code")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@DisplayName("배송 삭제")
	@Test
	void deleteDeliveryTest() throws Exception {
		DeliveryRequest.DeleteDeliveryRequest request = new DeliveryRequest.DeleteDeliveryRequest(
			savedDel1.getDeliveryId()
		);
		mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}
}
