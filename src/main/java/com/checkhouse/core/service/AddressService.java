package com.checkhouse.core.service;

import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.request.AddressRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    AddressDTO addAddress(AddressRequest.AddAddressRequest req) {
        Point addressLocation = null;
        //TODO: API로 경위도 받아오기

        //TODO: API로 주소 검증 코드 추가

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
}
