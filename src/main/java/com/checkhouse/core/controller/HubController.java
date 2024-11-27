package com.checkhouse.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.HubDTO;
import com.checkhouse.core.dto.StockDTO;
import com.checkhouse.core.dto.request.HubRequest;
import com.checkhouse.core.service.HubService;

@Slf4j
@Tag(name = "허브", description = "허브 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hubs")
public class HubController {
    private final HubService hubService;

    //허브 추가
    @Operation(summary = "허브 추가")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<HubDTO> addHub(
        @Valid @RequestBody HubRequest.AddHubRequest req) {
        log.info("[허브 추가] request: {}", req);
        return BaseResponse.onSuccess(hubService.addHub(req));
    }
    //허브 수정
    @Operation(summary = "허브 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{hubId}")
    public BaseResponse<HubDTO> updateHub(
        @Valid @RequestBody HubRequest.UpdateHubRequest req) {
        log.info("[허브 수정] request: {}", req);
        return BaseResponse.onSuccess(hubService.updateHub(req));
    }
    //허브 삭제
    @Operation(summary = "허브 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{hubId}")
    public BaseResponse<Void> deleteHub(
        @Valid @RequestBody HubRequest.DeleteHubRequest req) {
        log.info("[허브 삭제] request: {}", req);
        hubService.deleteHub(req);
        return BaseResponse.onSuccess(null);
    }
    //허브 조회
    @Operation(summary = "허브 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public BaseResponse<List<HubDTO>> getHubs() {
        log.info("[허브 조회]");
        return BaseResponse.onSuccess(hubService.getHubs());
    }
    // todo: 허브 할당
    // @Operation(summary = "허브 할당")
    // @ApiResponses({
    //     @ApiResponse(responseCode = "200", description = "할당 성공"),
    //     @ApiResponse(responseCode = "400", description = "잘못된 요청")
    // })
    // @PostMapping("/allocate")
    // public BaseResponse<HubDTO> allocateHub(
    //     @Valid @RequestBody HubRequest.AllocateHubRequest req) {
    //     try {
    //         log.info("[허브 할당] request: {}", req);
    //         return BaseResponse.onSuccess(hubService.allocateHub(req));
    //     } catch (Exception e) {
    //         log.error("[허브 할당] request: {}, error: {}", req, e.getMessage());
    //         return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
    //     }
    // }

    // 재고 추가
    @Operation(summary = "재고 추가")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/stock")
    public BaseResponse<StockDTO> addStock(
        @Valid @RequestBody HubRequest.AddStockRequest req) {
        log.info("[재고 추가] request: {}", req);
        return BaseResponse.onSuccess(hubService.addStock(req));
    }
    // 재고 수정
    @Operation(summary = "재고 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/stock/{stockId}")
    public BaseResponse<StockDTO> updateStock(
        @Valid @RequestBody HubRequest.UpdateStockAreaRequest req) {
        log.info("[재고 수정] request: {}", req);
        return BaseResponse.onSuccess(hubService.updateStockArea(req));
    }
    // 재고 조회
    @Operation(summary = "재고 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/stock/{usedProductId}")
    public BaseResponse<StockDTO> getStock(
        @Valid @RequestBody HubRequest.GetStockByUsedProductIdRequest req) {
        log.info("[재고 조회] request: {}", req);
        return BaseResponse.onSuccess(hubService.getStockByUsedProductId(req));
    }
    // 재고 조회(허브별)
    @Operation(summary = "재고 조회(허브별)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/stock/{hubId}")
    public BaseResponse<List<StockDTO>> getStocksByHubId(
        @Valid @RequestBody HubRequest.GetStocksByHubIdRequest req) {
        log.info("[재고 조회(허브별)] request: {}", req);
        return BaseResponse.onSuccess(hubService.getStocksByHubId(req));
    }
    // 재고 조회(지역별)
    @Operation(summary = "재고 조회(지역별)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/stock/{area}")
    public BaseResponse<List<StockDTO>> getStocksByArea(
        @Valid @RequestBody HubRequest.GetStocksByAreaRequest req) {
        log.info("[재고 조회(지역별)] request: {}", req);
        return BaseResponse.onSuccess(hubService.getStocksByArea(req));
    }
    // 재고 삭제
    @Operation(summary = "재고 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/stock/{stockId}")
    public BaseResponse<Void> deleteStock(
        @Valid @RequestBody HubRequest.DeleteStockRequest req) {
        log.info("[재고 삭제] request: {}", req);
        hubService.deleteStock(req);
        return BaseResponse.onSuccess(null);
    }
}
