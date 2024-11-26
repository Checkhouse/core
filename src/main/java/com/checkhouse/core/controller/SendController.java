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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "send apis", description = "발송 관련 API - 발송 등록, 발송 상태 업데이트, 발송 삭제, 발송 리스트 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/send")
public class SendController {
    private final SendService sendService;
    //발송 등록
    @Operation(summary = "발송 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<SendDTO> addSend(
        @RequestBody SendRequest.AddSendRequest req) {
        try {
            log.info("[발송 등록] request: {}", req);
            return BaseResponse.onSuccess(sendService.addSend(req));
        } catch (Exception e) {
            log.error("[발송 등록] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //발송 상태 업데이트
    @Operation(summary = "발송 상태 업데이트")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "업데이트 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/{sendId}")
    public BaseResponse<SendDTO> updateSendState(
        @RequestBody SendRequest.UpdateSendStateRequest req) {
        try {
            log.info("[발송 상태 업데이트] request: {}", req);
            return BaseResponse.onSuccess(sendService.updateSendState(req));
        } catch (Exception e) {
            log.error("[발송 상태 업데이트] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //발송 삭제
    @Operation(summary = "발송 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{sendId}")
    public BaseResponse<Void> deleteSend(
        @RequestBody SendRequest.DeleteSendRequest req) {
        try {
            log.info("[발송 삭제] request: {}", req);
            sendService.deleteSend(req);
            return BaseResponse.onSuccess(null);
        } catch (Exception e) {
            log.error("[발송 삭제] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //발송 리스트 조회
    @Operation(summary = "발송 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list/{transactionId}")
    public BaseResponse<List<SendDTO>> getSendList(
        @RequestParam UUID transactionId) {
        try {
            log.info("[발송 리스트 조회] request: {}", transactionId);
            return BaseResponse.onSuccess(sendService.getSendList(transactionId));
        } catch (Exception e) {
            log.error("[발송 리스트 조회] request: {}, error: {}", transactionId, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
}
