package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.StoreDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Store;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.StoreRepository;
import com.checkhouse.core.dto.request.StoreRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@SQLDelete(sql="")
public class StoreService {
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;

    //스토어 저장
    public StoreDTO addStore(StoreRequest.AddStoreRequest req) {
        storeRepository.findStoreByName(req.name()).ifPresent(store -> {
            throw new GeneralException(ErrorStatus._STORE_ALREADY_EXISTS);
        });
        Address addr = addressRepository.findById(req.address().getAddressId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND)
        );
        Store savedStore = storeRepository.save(
                Store.builder()
                        .storeId(req.storeId())
                        .name(req.name())
                        .address(addr)
                        .code(req.code())
                        .build()
        );

        return savedStore.toDto();
    }
    //스토어 이름으로 조회
    public StoreDTO getStoreByName(String name) {
        Store store = storeRepository.findStoreByName(name).orElseThrow(() -> new GeneralException(ErrorStatus._STORE_ID_NOT_FOUND));
        return store.toDto();
    }
    //스토어 조회
    public StoreDTO getStore(StoreRequest.GetStoreRequest req) {
        Store store = storeRepository.findById(req.storeId()).orElseThrow(() -> new GeneralException(ErrorStatus._STORE_ID_NOT_FOUND));
        return store.toDto();
    }
    //스토어 리스트 조회
    public List<StoreDTO> getStores() {
        return storeRepository.findAll()
                .stream()
                .map(Store::toDto)
                .toList();
    }
    //스토어 정보 수정
    public StoreDTO updateStore(StoreRequest.UpdateStoreRequest req) {
        Store store = storeRepository.findById(req.storeId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._STORE_ID_NOT_FOUND));
        storeRepository.findStoreByName(req.name()).ifPresent((a) -> {
            throw new GeneralException(ErrorStatus._STORE_ALREADY_EXISTS);
        });
        Address addr = addressRepository.findById(req.address().getAddressId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND)
        );
        store.updateName(req.name());
        store.updateAddress(addr);

        return store.toDto();
    }
    //스토어 코드 수정
    public StoreDTO updateStoreCode(StoreRequest.UpdateStoreCodeRequest req) {
        Store store = storeRepository.findById(req.storeId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._STORE_ID_NOT_FOUND));
        store.updateCode(req.code());
        return store.toDto();
    }
    //스토어 코드 확인
    public boolean verifyCode(StoreRequest.VerifyCodeRequest req) {
        Store store = storeRepository.findById(req.storeId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._STORE_ID_NOT_FOUND));
        return (store.getCode().equals(req.code()));
    }
    //스토어 삭제
    public void deleteStore(StoreRequest.DeleteStoreRequest req) {
        Store store = storeRepository.findById(req.storeId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._STORE_ID_NOT_FOUND));
        storeRepository.delete(store);
    }

}
