package com.checkhouse.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.checkhouse.core.entity.*;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.enums.UsedProductState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.SendDTO;
import com.checkhouse.core.dto.request.SendRequest;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.repository.mysql.SendRepository;
import com.checkhouse.core.repository.mysql.TransactionRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;

@ExtendWith(MockitoExtension.class)
public class SendServiceTest {
    @Mock
    private SendRepository sendRepository;

    @InjectMocks
    private SendService sendService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private UsedProductRepository usedProductRepository;

    private User seller;
    private User buyer;
    private OriginProduct originProduct1;
    private Delivery delivery1;
    private Delivery delivery2;
    private Transaction transaction1;
    private UsedProduct usedProduct1;
    private Transaction transaction2;
    private Send send1;
    private Send send2;


    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        Address address = Address.builder()
            .addressId(UUID.randomUUID())
            .build();

        delivery1 = Delivery.builder()
            .deliveryId(UUID.randomUUID())
            .address(address)
            .build();
        seller = User.builder()
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .role(Role.ROLE_USER)
                .build();

        buyer = User.builder()
                .userId(UUID.randomUUID())
                .username("test user2")
                .email("test2@test.com")
                .nickname("test nickname2")
                .role(Role.ROLE_USER)
                .build();

        originProduct1 = OriginProduct.builder()
                .id(UUID.randomUUID())
                .name("아이패드")
                .company("애플")
                .category(
                        Category.builder()
                                .name("category1")
                                .build()
                )
                .build();
        usedProduct1 = UsedProduct.builder()
                .usedProductId(UUID.randomUUID())
                .title("아이패드 떨이")
                .description("싸다싸 너만오면 고")
                .price(3000)
                .isNegoAllow(true)
                .state(UsedProductState.ON_SALE)
                .originProduct(originProduct1)
                .user(seller)
                .build();
        transaction1 = Transaction.builder()
            .transactionId(UUID.randomUUID())
                .usedProduct(usedProduct1)
                .buyer(buyer)
            .build();

        send1 = Send.builder()
            .sendId(UUID.randomUUID())
            .state(DeliveryState.PRE_DELIVERY)
            .transaction(transaction1)
            .delivery(delivery1)
            .build();
    }

    @DisplayName("발송 등록 성공")
    @Test
    void SUCCESS_addSend() {
        // given
        SendRequest.AddSendRequest req = SendRequest.AddSendRequest.builder()
            .transactionId(transaction1.getTransactionId())
            .build();

        when(transactionRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(transaction1));
        when(usedProductRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(usedProduct1));
        when(sendRepository.save(any(Send.class)))
            .thenReturn(send1);
        when(deliveryRepository.save(any(Delivery.class)))
            .thenReturn(delivery1);

        // when
        SendDTO result = sendService.addSend(req);

        // then
        assertEquals(send1.toDto(), result);
    }

    @DisplayName("발송 상태 수정")
    @Test
    void SUCCESS_updateSendState() {
        // 데이터 생성
        UUID sendId = send1.getSendId();
        DeliveryState state = DeliveryState.SENDING;

        SendRequest.UpdateSendStateRequest req = new SendRequest.UpdateSendStateRequest(sendId, state);

        // given
        when(sendRepository.findById(sendId))
            .thenReturn(Optional.of(send1));
        when(sendRepository.save(any(Send.class)))
            .thenReturn(send1);

        // when
        SendDTO result = sendService.updateSendState(req);

        // then
        assertEquals(send1.toDto(), result);
    }

    @DisplayName("존재하지 않는 중고 상품의 경우 발송 등록 실패")
    @Test
    void FAIL_addSend_invalid_usedProduct() {
        // given
        SendRequest.AddSendRequest req = SendRequest.AddSendRequest.builder()
            .transactionId(transaction1.getTransactionId())
            .build();
        
        when(transactionRepository.findById(any(UUID.class))).thenReturn(Optional.of(transaction1));
        when(usedProductRepository.findById(any(UUID.class))).thenReturn(Optional.empty());  // 중고 상품을 찾을 수 없음

        // when & then
        assertThrows(GeneralException.class, () -> sendService.addSend(req));
    }

    @DisplayName("존재하지 않는 거래일 경우 등록 실패")
    @Test
    void FAIL_addSend_not_found() {
        // 데이터 생성
        UUID transactionId = UUID.randomUUID();

        SendRequest.AddSendRequest req = SendRequest.AddSendRequest.builder()
            .transactionId(transactionId)
            .build();

        // given
        when(transactionRepository.findById(transactionId))
            .thenReturn(Optional.empty());

        // when
        assertThrows(GeneralException.class, () -> sendService.addSend(req));
    }

    @DisplayName("존재하지 않는 발송 상태로 수정하는 경우 실패")
    @Test
    void FAIL_updateSendStatus_invalid_status() {
        // 데이터 생성
        UUID sendId = send1.getSendId();
        DeliveryState state = DeliveryState.DELIVERED;

        SendRequest.UpdateSendStateRequest req = new SendRequest.UpdateSendStateRequest(sendId, state);

        // given
        when(sendRepository.findById(sendId))
            .thenReturn(Optional.of(send1));

        // when
        assertThrows(GeneralException.class, () -> sendService.updateSendState(req));
        
    }
    @DisplayName("존재하지 않는 발송 ID를 수정하는 경우 실패")
    @Test
    void FAIL_updateSendStatus_invalid_sendId() {
        // 데이터 생성
        UUID sendId = UUID.randomUUID();
        DeliveryState state = DeliveryState.SENDING;

        SendRequest.UpdateSendStateRequest req = new SendRequest.UpdateSendStateRequest(sendId, state);

        // given
        when(sendRepository.findById(sendId))
            .thenReturn(Optional.empty());

        // when
        assertThrows(GeneralException.class, () -> sendService.updateSendState(req));
        
    }
    @DisplayName("이미 발송 등록이 된 상품은 발송 등록 실패")
    @Test
    void FAIL_addSend_already_sent() {
        // given
        SendRequest.AddSendRequest req = SendRequest.AddSendRequest.builder()
            .transactionId(transaction1.getTransactionId())
            .build();

        when(transactionRepository.findById(req.transactionId()))
            .thenReturn(Optional.of(transaction1));
        when(sendRepository.findAllByTransactionTransactionId(transaction1.getTransactionId()))
            .thenReturn(List.of(send1));  // 이미 발송이 존재함

        // when & then
        assertThrows(GeneralException.class, () -> sendService.addSend(req));   
    }
}
