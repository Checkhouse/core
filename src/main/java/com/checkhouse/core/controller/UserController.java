package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.code.status.SuccessStatus;
import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.dto.UserAddressDTO;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.AddressRequest;
import com.checkhouse.core.dto.request.UserRequest;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.entity.es.HubDocument;
import com.checkhouse.core.entity.es.ZoneDocument;
import com.checkhouse.core.service.AddressService;
import com.checkhouse.core.service.HubEsService;
import com.checkhouse.core.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Tag(name = "user apis")
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AddressService addressService;
    private final HubEsService hubEsService;
    @GetMapping
    public BaseResponse<UserDTO> getUserInfo() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(userEmail);
        UserDTO user = userService.getUserInfo(userEmail);

        return BaseResponse.onSuccess(user);
    }
    @PostMapping
    public BaseResponse<UserDTO> saveUser(
            @RequestBody UserRequest.AddUserRequest request
    ) {
        UserDTO user = userService.addUser(request);
        return BaseResponse.onSuccess(user);
    }

    @GetMapping("/address")
    public BaseResponse<List<UserAddressDTO>> getUserAddressList() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO user = userService.getUserInfo(userEmail);

        List<UserAddressDTO> addressList = addressService.getAllUserAddressesById(
                AddressRequest.GetAllUserAddressesByIdRequest.builder()
                        .userId(user.userId())
                        .build()
        );

        return BaseResponse.onSuccess(addressList);
    }
    @PostMapping("/address")
    public BaseResponse<UserAddressDTO> addUserAddress(
            @Valid @RequestBody AddressRequest.AddAddressRequest request
    ) {
        // 인증자 정보 확인
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO user = userService.getUserInfo(userEmail);
        log.info("{}, {}", request.location().getX(), request.location().getY());
        ZoneDocument zone = hubEsService.searchZone(request.location().getX(), request.location().getY());
        log.info("{}", zone.getZoneId());
        HubDocument hub = hubEsService.searchHub(zone.getZoneId());
        log.info("{}", hub.getHubId());
        UserAddressDTO userAddress = addressService.addUserAddress(
                AddressRequest.AddUserAddressRequest.builder()
                        .userId(user.userId())
                        .name(request.name())
                        .address(request.address())
                        .zipcode(request.zipcode())
                        .phone(request.phone())
                        .addressDetail(request.addressDetail())
                        .location(request.location())
                        .hubId(UUID.fromString(hub.getHubId()))
                        .build()
        );

        return BaseResponse.onSuccess(userAddress);

    }

}
