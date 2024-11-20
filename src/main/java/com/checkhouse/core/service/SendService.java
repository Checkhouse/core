package com.checkhouse.core.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.checkhouse.core.dto.SendDTO;
import com.checkhouse.core.dto.request.SendRequest;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.Send;
import com.checkhouse.core.entity.Transaction;
// import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.repository.mysql.SendRepository;
import com.checkhouse.core.repository.mysql.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {
    private final SendRepository sendRepository;
    private final TransactionRepository transactionRepository;
    private final DeliveryRepository deliveryRepository;
    //발송 등록    
    SendDTO registerSend(SendRequest.RegisterSendRequest req) {
        //존재하지 않는 배송 정보가 있을 수 있으므로 예외처리
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
        .orElseThrow(() -> new RuntimeException("Delivery not found"));
        //존재하지 않는 거래 정보가 있을 수 있으므로 예외처리
        Transaction transaction = transactionRepository.findById(req.transactionId())
        .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Send savedSend = sendRepository.save(
            Send.builder()
                .transaction(transaction)
                .delivery(delivery)
                .state(DeliveryState.PRE_DELIVERY)
                .build()
        );
        return savedSend.toDTO();
    }
    //발송 상태 수정
    SendDTO updateSendState(UUID sendId, SendRequest.UpdateSendStateRequest req) {
        //존재하지 않는 발송 정보가 있을 수 있으므로 예외처리
        Send send = sendRepository.findById(sendId)
        .orElseThrow(() -> new RuntimeException("Send not found"));
        //발송 상태 수정
        send.updateSendState(req.deliveryState());
        SendDTO updatedSend = sendRepository.save(send).toDTO();
        return updatedSend;
    }
}
