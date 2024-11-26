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
import com.checkhouse.core.dto.DeliveryDTO;
import com.checkhouse.core.dto.request.DeliveryRequest;
import com.checkhouse.core.service.DeliveryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "delivery apis")
@RestController
@RequiredArgsConstructor
@RequestMapping("delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;
    //배송 등록
    @PostMapping
    public BaseResponse<DeliveryDTO> addDelivery(@RequestBody DeliveryRequest.AddDeliveryRequest req) {
        log.info(req.toString());
        return BaseResponse.onSuccess(deliveryService.addDelivery(req));
    }
    //배송 상태 업데이트
    @PatchMapping
    public BaseResponse<DeliveryDTO> updateDeliveryState(@RequestBody DeliveryRequest.UpdateDeliveryStateRequest req) {
        log.info(req.toString());
        return BaseResponse.onSuccess(deliveryService.updateDeliveryState(req));
    }
    //배송 리스트 조회
    @GetMapping
    public BaseResponse<List<DeliveryDTO>> getDeliveryList() {
        log.info("getDeliveryList");
        return BaseResponse.onSuccess(deliveryService.getDeliveryList());
    }
    //송장 번호 등록
    @PostMapping("/tracking-code")
    public BaseResponse<DeliveryDTO> registerTrackingCode(@RequestBody DeliveryRequest.RegisterTrackingCodeRequest req) {
        log.info(req.toString());
        return BaseResponse.onSuccess(deliveryService.registerTrackingCode(req));
    }
    //배송 삭제
    @DeleteMapping
    public BaseResponse<Void> deleteDelivery(@RequestBody DeliveryRequest.DeleteDeliveryRequest req) {
        log.info(req.toString());
        deliveryService.deleteDelivery(req);
        return BaseResponse.onSuccess(null);
    }
}
