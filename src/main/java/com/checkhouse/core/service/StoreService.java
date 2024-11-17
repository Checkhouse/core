package com.checkhouse.core.service;

import com.checkhouse.core.dto.StoreDTO;
import com.checkhouse.core.repository.mysql.StoreRepository;
import com.checkhouse.core.request.StoreRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@SQLDelete(sql="")
public class StoreService {
    private final StoreRepository storeRepository;

    //스토어 저장
    StoreDTO addStore(StoreRequest.AddStoreRequest req) { return null; }
    //스토어 이름으로 조회
    StoreDTO getStoreByName(String name) { return null; }
    //스토어 조회
    StoreDTO getStore(UUID storeId) { return null; }
    //스토어 리스트 조회
    List<StoreDTO> getStores() { return null; }
    //스토어 정보 수정
    StoreDTO updateStore(StoreRequest.UpdateStoreRequest req) { return null; }
    //스토어 코드 수정
    StoreDTO updateStoreCode(StoreRequest.UpdateStoreCodeRequest req) { return null; }
    //스토어 코드 확인
    boolean verifyCode(StoreRequest.VerifyCodeRequest req) { return false; }
    //스토어 삭제
    void deleteStore(StoreRequest.DeleteStoreRequest req) { storeRepository.deleteById(req.storeId()); }

}
