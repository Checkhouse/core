package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.dto.UserAddressDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.UserAddress;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.dto.request.AddressRequest;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.repository.mysql.UserAddressRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;
    private final HubRepository hubRepository;


    //주소 추가
    public AddressDTO addAddress(AddressRequest.AddAddressRequest req) {

        Address savedAddress = addressRepository.save(
                Address.builder()
                        .name(req.name())
                        .address(req.address())
                        .zipcode(req.zipcode())
                        .phone(req.phone())
                        .addressDetail(req.addressDetail())
                        .location(req.location())
                        .build()
        );

        return savedAddress.toDto();
    }

    //리스트 가져오기
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(Address::toDto)
                .collect(Collectors.toList());
    }

    //Id로 가져오기
    public AddressDTO getAddressById(AddressRequest.GetAddressByIdRequest req) {
        Address address = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

        return address.toDto();
    }

    //Update
    public AddressDTO updateAddress(AddressRequest.UpdateAddressRequest req) {
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

        return modifiedAddress.toDto();
    }

    public void deleteAddress(AddressRequest.DeleteAddressRequest req) {
        Address address = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

        addressRepository.delete(address);
    }

    //UserAddress
    public UserAddressDTO addUserAddress(AddressRequest.AddUserAddressRequest req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        Hub hub = hubRepository.findById(req.hubId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND));

        Address address = addressRepository.save(
                Address.builder()
                        .name(req.name())
                        .address(req.address())
                        .zipcode(req.zipcode())
                        .phone(req.phone())
                        .addressDetail(req.addressDetail())
                        .location(req.location())
                        .build()
        );
        UserAddress userAddress = userAddressRepository.save(
                UserAddress.builder()
                        .userAddressId(req.userAddressId())
                        .address(address)
                        .user(user)
                        .hub(hub)
                        .build()
        );
        return userAddress.toDto();
    }
    public UserAddressDTO updateUserAddressHub(AddressRequest.UpdateUserAddressHubRequest req) {
        UserAddress userAddress = userAddressRepository.findById(req.userAddressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_ADDRESS_ID_NOT_FOUND));
        Hub newhub = hubRepository.findById(req.hubId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND));
        userAddress.UpdateHub(newhub);
        return userAddress.toDto();
    }
    public UserAddressDTO getUserAddress(AddressRequest.GetUserAddressRequest req) {
        UserAddress userAddress = userAddressRepository.findById(req.userAddressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_ADDRESS_ID_NOT_FOUND));
        return userAddress.toDto();
    }
    public List<UserAddressDTO> getAllUserAddressesById(AddressRequest.GetAllUserAddressesByIdRequest req) {
        UUID userId = req.userId();
        userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );
        return userAddressRepository.findAllByUserUserId(userId)
                .stream()
                .map(UserAddress::toDto)
                .collect(Collectors.toList());
    }
    public  void deleteUserAddress(AddressRequest.DeleteUserAddressRequest req) {
        UserAddress userAddress = userAddressRepository.findById(req.userAddressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_ADDRESS_ID_NOT_FOUND));
        userAddressRepository.delete(userAddress);
    }

}

