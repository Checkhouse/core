package com.checkhouse.core.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.dto.SendDTO;
import com.checkhouse.core.dto.request.SendRequest;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.Send;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.apiPayload.exception.GeneralException;
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
    SendDTO addSend(SendRequest.AddSendRequest req) {
        //존재하지 않는 배송 정보가 있을 수 있으므로 예외처리
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
        .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));
        //존재하지 않는 거래 정보가 있을 수 있으므로 예외처리
        Transaction transaction = transactionRepository.findById(req.transactionId())
        .orElseThrow(() -> new GeneralException(ErrorStatus._TRANSACTION_ID_NOT_FOUND));

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
    SendDTO updateSendState(SendRequest.UpdateSendStateRequest req) {
        //존재하지 않는 발송 정보가 있을 수 있으므로 예외처리
        Send send = sendRepository.findById(req.sendId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._SEND_ID_NOT_FOUND));
        //존재하지 않는 발송 상태가 있을 수 있으므로 예외처리
        if(!req.deliveryState().equals(DeliveryState.PRE_DELIVERY) && !req.deliveryState().equals(DeliveryState.SENDING)) {
            throw new GeneralException(ErrorStatus._SEND_STATE_CHANGE_FAILED);
        }
        //존재하지 않는 발송 ID 수정 시 예외처리
        if(req.sendId() == null) {
            throw new GeneralException(ErrorStatus._SEND_ID_NOT_FOUND);
        }
        //발송 상태 수정
        send.updateSendState(req.deliveryState());
        SendDTO updatedSend = sendRepository.save(send).toDTO();
        return updatedSend;
    }
}
