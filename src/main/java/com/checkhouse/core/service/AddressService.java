package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.request.AddressRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    //TODO: API로 경위도 받아오기, Zipcode도 받아올 수 있긴 할듯
    //TODO: API로 주소 검증 코드 추가 (필요할까?) 경위도 받아오면서 검증도 같이하면 될듯
    private Point getAddressLocation(String address) {
        return new Point(0, 0);
    }

    //주소 추가
    AddressDTO addAddress(AddressRequest.AddAddressRequest req) {
        Point addressLocation = getAddressLocation(req.getAddress());

        Address savedAddress = addressRepository.save(
                Address.builder()
                        .addressId(req.getAddressId())
                        .name(req.getName())
                        .address(req.getAddress())
                        .zipcode(req.getZipcode())
                        .phone(req.getPhone())
                        .addressDetail(req.getAddressDetail())
                        .location(addressLocation)
                        .build()
        );

        return savedAddress.toDTO();
    }

    //리스트 가져오기
    List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(Address::toDTO)
                .collect(Collectors.toList());
    }

    //Id로 가져오기
    AddressDTO getAddressById(UUID addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

        return address.toDTO();
    }

    //Update
    AddressDTO updateAddress(UUID addressId, AddressRequest.UpdateAddressRequest req) {
        Address modifiedAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

        Point addressLocation = getAddressLocation(req.getAddress());

        modifiedAddress.update(
                req.getName() != null ? req.getName() : modifiedAddress.getName(),
                req.getAddress() != null ? req.getAddress() : modifiedAddress.getAddress(),
                req.getAddressDetail() != null ? req.getAddressDetail() : modifiedAddress.getAddressDetail(),
                req.getZipcode() != 0 ? req.getZipcode() : modifiedAddress.getZipcode(), // 0으로 초기화된 int 처리
                addressLocation,
                req.getPhone() != null ? req.getPhone() : modifiedAddress.getPhone()
        );

        return modifiedAddress.toDTO();
    }

    //Soft delete (유저 뷰)
    void deleteAddress(UUID addressId) {
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

        address.setDeleted();
    }

    @Query(value = "DELETE FROM Address a WHERE a.deletedDate IS NOT NULL", nativeQuery = true)
    //TODO: hard delete test
    void hardDeleteAddress() {

    }


}
