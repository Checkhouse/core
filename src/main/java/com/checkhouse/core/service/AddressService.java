package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.dto.request.AddressRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Point addressLocation = getAddressLocation(req.address());

        Address savedAddress = addressRepository.save(
                Address.builder()
                        .addressId(req.addressId())
                        .name(req.name())
                        .address(req.address())
                        .zipcode(req.zipcode())
                        .phone(req.phone())
                        .addressDetail(req.addressDetail())
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
    AddressDTO getAddressById(AddressRequest.GetAddressByIdRequest req) {
        Address address = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

        return address.toDTO();
    }

    //Update
    AddressDTO updateAddress(AddressRequest.UpdateAddressRequest req) {
        Address modifiedAddress = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

        Point addressLocation = getAddressLocation(req.address());

        modifiedAddress.update(
                req.name() != null ? req.name() : modifiedAddress.getName(),
                req.name() != null ? req.name() : modifiedAddress.getAddress(),
                req.addressDetail() != null ? req.addressDetail() : modifiedAddress.getAddressDetail(),
                req.zipcode() != 0 ? req.zipcode() : modifiedAddress.getZipcode(), // 0으로 초기화된 int 처리
                addressLocation,
                req.phone() != null ? req.phone() : modifiedAddress.getPhone()
        );

        return modifiedAddress.toDTO();
    }

    //Soft delete (유저 뷰)
    void deleteAddress(AddressRequest.DeleteAddressRequest req) {
        Address address = addressRepository.findById(req.addressId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

        addressRepository.delete(address);
    }

}
