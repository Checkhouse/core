package com.checkhouse.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.CollectDTO;
import com.checkhouse.core.dto.request.CollectRequest;
import com.checkhouse.core.service.CollectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "collect apis")
@RestController
@RequiredArgsConstructor
@RequestMapping("collect")
public class CollectController {
    private final CollectService collectService;

    //수거 등록
    @PostMapping
    public BaseResponse<CollectDTO> addCollect(@RequestBody CollectRequest.AddCollectRequest req) {
        return BaseResponse.onSuccess(collectService.addCollect(req));
    }
    //수거 상태 업데이트
    @PatchMapping
    public BaseResponse<CollectDTO> updateCollectState(@RequestBody CollectRequest.UpdateCollectRequest req) {
        return BaseResponse.onSuccess(collectService.updateCollect(req));
    }
    //수거 리스트 조회
    @GetMapping
    public BaseResponse<List<CollectDTO>> getCollectList() {
        return BaseResponse.onSuccess(collectService.getCollectList());
    }
    //수거 삭제
    @DeleteMapping
    public BaseResponse<Void> deleteCollect(@RequestBody CollectRequest.DeleteCollectRequest req) {
        collectService.deleteCollect(req);
        return BaseResponse.onSuccess(null);
    }
}
