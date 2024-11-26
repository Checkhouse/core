package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.SendDTO;
import com.checkhouse.core.dto.request.SendRequest;
import com.checkhouse.core.service.SendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "send apis")
@RestController
@RequiredArgsConstructor
@RequestMapping("send")
public class SendController {
    private final SendService sendService;
    //발송 등록
    @PostMapping
    public BaseResponse<SendDTO> addSend(@RequestBody SendRequest.AddSendRequest req) {
        return BaseResponse.onSuccess(sendService.addSend(req));
    }
    //발송 상태 업데이트
    @PatchMapping
    public BaseResponse<SendDTO> updateSendState(@RequestBody SendRequest.UpdateSendStateRequest req) {
        return BaseResponse.onSuccess(sendService.updateSendState(req));
    }
    //발송 삭제
    @DeleteMapping
    public BaseResponse<Void> deleteSend(@RequestBody SendRequest.DeleteSendRequest req) {
        sendService.deleteSend(req);
        return BaseResponse.onSuccess(null);
    }
    //발송 리스트 조회
    @GetMapping
    public BaseResponse<List<SendDTO>> getSendList(@RequestParam UUID transactionId) {
        return BaseResponse.onSuccess(sendService.getSendList(transactionId));
    }
}
