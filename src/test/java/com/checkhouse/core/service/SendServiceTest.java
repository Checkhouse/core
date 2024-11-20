package com.checkhouse.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.checkhouse.core.dto.SendDTO;
import com.checkhouse.core.dto.request.SendRequest;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.Send;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.repository.mysql.SendRepository;
import com.checkhouse.core.repository.mysql.TransactionRepository;

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

    private Delivery delivery1;
    private Delivery delivery2;
    private Transaction transaction1;
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

        transaction1 = Transaction.builder()
            .transactionId(UUID.randomUUID())
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
        // 데이터 생성
        UUID deliveryId = delivery1.getDeliveryId();
        UUID transactionId = transaction1.getTransactionId();
        UUID sendId = send1.getSendId();

        SendRequest.RegisterSendRequest req = new SendRequest.RegisterSendRequest(
            sendId,
            transactionId,
            deliveryId
        );

        // given
        when(deliveryRepository.findById(deliveryId))
            .thenReturn(Optional.of(delivery1));
        when(transactionRepository.findById(transactionId))
            .thenReturn(Optional.of(transaction1));
        when(sendRepository.save(any(Send.class)))
            .thenReturn(send1);

        // when
        SendDTO result = sendService.registerSend(req);

        // then
        assertEquals(send1.toDTO(), result);
    }

    @DisplayName("발송 상태 수정")
    @Test
    void SUCCESS_updateSendState() {
        // 데이터 생성
        UUID sendId = send1.getSendId();
        DeliveryState state = DeliveryState.SENDING;

        SendRequest.UpdateSendStateRequest req = new SendRequest.UpdateSendStateRequest(state);

        // given
        when(sendRepository.findById(sendId))
            .thenReturn(Optional.of(send1));
        when(sendRepository.save(any(Send.class)))
            .thenReturn(send1);

        // when
        SendDTO result = sendService.updateSendState(sendId, req);

        // then
        assertEquals(send1.toDTO(), result);
    }

    @DisplayName("존재하지 않는 배송일 경우 등록 실패")
    @Test
    void FAIL_addSend_invalid_delivery() {
        // 데이터 생성
        UUID deliveryId = UUID.randomUUID();
        UUID transactionId = transaction1.getTransactionId();
        UUID sendId = send1.getSendId();

        SendRequest.RegisterSendRequest req = new SendRequest.RegisterSendRequest(
            sendId,
            transactionId,
            deliveryId
        );

        // given
        when(deliveryRepository.findById(deliveryId))
            .thenReturn(Optional.empty());

        // when
        assertThrows(RuntimeException.class, () -> sendService.registerSend(req));
        
    }

    @DisplayName("존재하지 않는 거래일 경우 등록 실패")
    @Test
    void FAIL_addSend_not_found() {
        // 데이터 생성
        UUID deliveryId = delivery1.getDeliveryId();
        UUID transactionId = UUID.randomUUID();
        UUID sendId = send1.getSendId();

        SendRequest.RegisterSendRequest req = new SendRequest.RegisterSendRequest(
            sendId,
            transactionId,
            deliveryId
        );

        // given
        when(deliveryRepository.findById(deliveryId))
            .thenReturn(Optional.of(delivery1));
        when(transactionRepository.findById(transactionId))
            .thenReturn(Optional.empty());

        // when
        assertThrows(RuntimeException.class, () -> sendService.registerSend(req));
    }

    @DisplayName("존재하지 않는 발송 상태로 수정하는 경우 실패")
    @Test
    void FAIL_updateSendStatus_invalid_status() {
        // 데이터 생성
        UUID sendId = send1.getSendId();
        DeliveryState state = DeliveryState.DELIVERED;

        SendRequest.UpdateSendStateRequest req = new SendRequest.UpdateSendStateRequest(state);

        // given
        when(sendRepository.findById(sendId))
            .thenReturn(Optional.of(send1));

        // when
        assertThrows(RuntimeException.class, () -> sendService.updateSendState(sendId, req));
    }
}
