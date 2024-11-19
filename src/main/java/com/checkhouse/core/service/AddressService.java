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


    //주소 추가
    AddressDTO addAddress(AddressRequest.AddAddressRequest req) {

        Address savedAddress = addressRepository.save(
                Address.builder()
                        .addressId(req.addressId())
                        .name(req.name())
                        .address(req.address())
                        .zipcode(req.zipcode())
                        .phone(req.phone())
                        .addressDetail(req.addressDetail())
                        .location(req.location())
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


        modifiedAddress.update(
                req.name() != null ? req.name() : modifiedAddress.getName(),
                req.address() != null ? req.address() : modifiedAddress.getAddress(),
                req.addressDetail() != null ? req.addressDetail() : modifiedAddress.getAddressDetail(),
                req.zipcode() != 0 ? req.zipcode() : modifiedAddress.getZipcode(), // 0으로 초기화된 int 처리
                req.location() != null ? req.location() : modifiedAddress.getLocation(),
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
