package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.dto.UserAddressDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.UserAddress;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.dto.request.AddressRequest;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.repository.mysql.UserAddressRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Point;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserAddressRepository userAddressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HubRepository hubRepository;


    @InjectMocks
    private AddressService addressService;

    private Address commonAddress;
    private Address otherAddress;
    private UserAddress userAddress;
    private User user;
    private Hub hub;
    private Hub hub2;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        commonAddress = Address.builder()
                .addressId(UUID.randomUUID())
                .name("한글이름")
                .address("서울특별시 동작구 상도로 369")
                .zipcode(6978)
                .phone("01097063979")
                .addressDetail("정보과학관 지하 1층")
                .location(new Point(0, 0))
                .build();
        otherAddress = Address.builder()
                .addressId(UUID.randomUUID())
                .name("이름입니다")
                .address("서울특별시 동작구 상도1동")
                .zipcode(12345)
                .phone("01023456789")
                .addressDetail("2220호")
                .location(new Point(0, 0))
                .build();
        user = User.builder()
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
                .build();
        hub = Hub.builder()
                .hubId(UUID.randomUUID())
                .address(otherAddress)
                .name("허브1")
                .clusteredId(1)
                .build();
        hub2 = Hub.builder()
                .hubId(UUID.randomUUID())
                .address(commonAddress)
                .name("허브2")
                .clusteredId(2)
                .build();
        userAddress = UserAddress.builder()
                .userAddressId(UUID.randomUUID())
                .address(commonAddress)
                .user(user)
                .hub(hub)
                .build();

    }

    @DisplayName("주소 등록 성공")
    @Test
    void SUCCESS_addAddress() {
        UUID randomUUID = UUID.randomUUID();
        AddressRequest.AddAddressRequest req = AddressRequest.AddAddressRequest.builder()
                .addressId(randomUUID)
                .name(commonAddress.getName())
                .address(commonAddress.getAddress())
                .zipcode(commonAddress.getZipcode())
                .phone(commonAddress.getPhone())
                .build();

        //특정 uuid를 넣으면, 다음과 같이 동작 할 것이다.
        when(addressRepository.save(any(Address.class))).thenReturn(commonAddress);

        AddressDTO result = addressService.AddAddress(req);
        assertNotNull(result);
        assertEquals("한글이름", result.name());
        assertEquals("서울특별시 동작구 상도로 369", result.address());
        assertEquals(6978, result.zipcode());
        assertEquals("01097063979", result.phone());


    }

    @DisplayName("주소 일부 수정 성공")
    @Test
    void SUCCESS_updateSomeAddress() {
        UUID addressId = commonAddress.getAddressId();
        //이름이랑 전화번호만 바꾸기
        AddressRequest.UpdateAddressRequest req =
                AddressRequest.UpdateAddressRequest.builder()
                        .addressId(addressId)
                        .name("홍길동")
                        .phone("01012345678")
                        .build();
        when(addressRepository.findById(any(UUID.class))).thenReturn(Optional.of(commonAddress));

        AddressDTO result = addressService.UpdateAddress(req);

        assertNotNull(result);
        assertEquals("홍길동", result.name());
        assertEquals("01012345678", result.phone());
        assertEquals(commonAddress.getZipcode(), result.zipcode());

        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @DisplayName("주소 수정 성공")
    @Test
    void SUCCESS_updateAddress() {
        UUID addressId = commonAddress.getAddressId();
        //이름이랑 전화번호만 바꾸기
        AddressRequest.UpdateAddressRequest req =
                AddressRequest.UpdateAddressRequest.builder()
                        .addressId(addressId)
                        .name("홍길동")
                        .phone("01012345678")
                        .address("서울특별시 동작구 상도로 45길")
                        .addressDetail("어딘가 횡단보도 앞")
                        .zipcode(12345)
                        .phone("01012345678")
                        .build();
        when(addressRepository.findById(any(UUID.class))).thenReturn(Optional.of(commonAddress));

        AddressDTO result = addressService.UpdateAddress(req);

        assertNotNull(result);
        assertEquals("서울특별시 동작구 상도로 45길", result.address());
        assertEquals("홍길동", result.name());
        assertEquals("01012345678", result.phone());
        assertEquals(12345, result.zipcode());
        assertEquals("어딘가 횡단보도 앞", result.addressDetail());

        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @DisplayName("주소 삭제 성공")
    @Test
    void SUCCESS_deleteAddress() {

        UUID addressId = commonAddress.getAddressId();
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(commonAddress));

        //when
        AddressRequest.DeleteAddressRequest req = AddressRequest.DeleteAddressRequest.builder().addressId(addressId).build();
        addressService.DeleteAddress(req);

        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).delete(any(Address.class));
    }

    @DisplayName("주소 조회 성공")
    @Test
    void SUCCESS_getAddress() {
        UUID addressId = commonAddress.getAddressId();
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(commonAddress));

        AddressRequest.GetAddressByIdRequest req = AddressRequest.GetAddressByIdRequest.builder().addressId(addressId).build();
        AddressDTO result = addressService.GetAddressById(req);

        assertNotNull(result);
        assertEquals(commonAddress.getAddressId(), result.addressId());
        assertEquals(commonAddress.getName(), result.name());
        assertEquals(commonAddress.getAddress(), result.address());
        assertEquals(commonAddress.getZipcode(), result.zipcode());
        assertEquals(commonAddress.getAddressDetail(), result.addressDetail());
        assertEquals(commonAddress.getPhone(), result.phone());

        verify(addressRepository, times(1)).findById(addressId);
    }

    @DisplayName("주소 리스트 조회 성공")
    @Test
    void SUCCESS_getAddressList() {
        when(addressRepository.findAll()).thenReturn(List.of(commonAddress, otherAddress));

        List<AddressDTO> result = addressService.GetAllAddresses();


        assertNotNull(result);
        assertEquals(2, result.size());

        // Check First data
        AddressDTO address1 = result.get(0);
        assertEquals(commonAddress.getAddressId(), address1.addressId());
        assertEquals(commonAddress.getName(), address1.name());
        assertEquals(commonAddress.getAddress(), address1.address());
        assertEquals(commonAddress.getZipcode(), address1.zipcode());
        assertEquals(commonAddress.getPhone(), address1.phone());

        // Check Second Data
        AddressDTO address2 = result.get(1);
        assertEquals(otherAddress.getAddressId(), address2.addressId());
        assertEquals(otherAddress.getName(), address2.name());
        assertEquals(otherAddress.getAddress(), address2.address());
        assertEquals(otherAddress.getZipcode(), address2.zipcode());
        assertEquals(otherAddress.getPhone(), address2.phone());
    }

    //UserAddress
    @DisplayName("유저 배송지 추가 성공")
    @Test
    void SUCCESS_addUserAddress() {
        //유저배송지
        AddressRequest.AddUserAddressRequest req = AddressRequest.AddUserAddressRequest.builder()
                .userAddressId(userAddress.getUserAddressId())
                .userId(user.getUserId())
                .name(commonAddress.getName())
                .address(commonAddress.getAddress())
                .zipcode(commonAddress.getZipcode())
                .phone(commonAddress.getPhone())
                .addressDetail(commonAddress.getAddressDetail())
                .location(commonAddress.getLocation())
                .build();

        //given
        when(addressRepository.save(any(Address.class))).thenReturn(commonAddress);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(hubRepository.findById(any())).thenReturn(Optional.of(hub));
        when(userAddressRepository.save(any(UserAddress.class))).thenReturn(userAddress);

        //when
        UserAddressDTO result = addressService.AddUserAddress(req);

        //then
        assertNotNull(result);
        assertEquals(result.userAddressId(), req.userAddressId());
        assertEquals(result.address().addressId(), commonAddress.getAddressId());
        assertEquals(result.user().userId(), user.getUserId());

        verify(addressRepository, times(1)).save(any(Address.class));
        verify(userRepository, times(1)).findById(user.getUserId());
        verify(hubRepository, times(1)).findById(any());
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }
    @DisplayName("유저 배송지 허브 수정 성공")
    @Test
    void SUCCESS_updateUserAddressHub() {
        //유저배송지
        AddressRequest.UpdateUserAddressHubRequest req = AddressRequest.UpdateUserAddressHubRequest.builder()
                .userAddressId(userAddress.getUserAddressId())
                        .hubId(hub2.getHubId())
                                .build();

        //given
        when(userAddressRepository.findById(any())).thenReturn(Optional.of(userAddress));
        when(hubRepository.findById(any())).thenReturn(Optional.of(hub2));

        //when
        UserAddressDTO result = addressService.UpdateUserAddressHub(req);

        //then
        assertNotNull(result);
        assertEquals(result.userAddressId(), req.userAddressId());
        assertEquals(result.address().addressId(), commonAddress.getAddressId());
        assertEquals(result.user().userId(), user.getUserId());
        assertEquals(result.hub().hubId(), hub2.getHubId());

        verify(userAddressRepository, times(1)).findById(userAddress.getUserAddressId());
        verify(hubRepository, times(1)).findById(any());
    }
    @DisplayName("유저 배송지 조회 성공")
    @Test
    void SUCCESS_getUserAddress() {
        //유저배송지
        UUID userAddressId = userAddress.getUserAddressId();
        AddressRequest.GetUserAddressRequest req = AddressRequest.GetUserAddressRequest.builder()
                .userAddressId(userAddressId)
                        .build();

        //given
        when(userAddressRepository.findById(userAddressId)).thenReturn(Optional.of(userAddress));

        //when
        UserAddressDTO result = addressService.GetUserAddress(req);

        //then
        assertNotNull(result);
        assertEquals(userAddressId, result.userAddressId());
        assertEquals(userAddress.getAddress().getAddressId(), result.address().addressId());

        verify(userAddressRepository, times(1)).findById(userAddressId);
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }
    @DisplayName("특정 유저 배송지 리스트 조회 성공")
    @Test
    void SUCCESS_getAllUserAddressesById() {
        //유저배송지
        UUID userId = user.getUserId();

        AddressRequest.GetAllUserAddressesByIdRequest req = AddressRequest.GetAllUserAddressesByIdRequest.builder()
                .userId(userId)
                .build();

        //Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userAddressRepository.findAllByUserUserId(userId)).thenReturn(List.of(userAddress));

        //when
        List<UserAddressDTO> result = addressService.GetAllUserAddressesById(req);

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        UserAddressDTO userAddressDTO = result.get(0);
        assertEquals(userAddress.getUserAddressId(), userAddressDTO.userAddressId());
        assertEquals(userAddress.getAddress().getAddressId(), userAddressDTO.address().addressId());
        assertEquals(userAddress.getUser().getUserId(), userAddressDTO.user().userId());

        verify(userRepository, times(1)).findById(userId);
        verify(userAddressRepository, times(1)).findAllByUserUserId(userId);

    }
    @DisplayName("유저 배송지 삭제 성공")
    @Test
    void SUCCESS_deleteUserAddress() {
        //유저 배송지
        UUID userAddressId = userAddress.getUserAddressId();

        AddressRequest.DeleteUserAddressRequest req = AddressRequest.DeleteUserAddressRequest.builder()
                .userAddressId(userAddressId)
                .build();

        //Given
        when(userAddressRepository.findById(userAddressId)).thenReturn(Optional.of(userAddress));

        //when
        addressService.DeleteUserAddress(req);

        //then
        verify(userAddressRepository, times(1)).findById(userAddressId);
        verify(userAddressRepository, times(1)).delete(userAddress);
    }

    //--------------------------Fail------------------------

    @DisplayName("존재하지 않는 주소의 경우 조회 실패")
    @Test
    void FAIL_getAddress_not_found() {
        //존재하지 않는 id
        UUID invalidUUID = UUID.randomUUID();
        when(addressRepository.findById(invalidUUID)).thenReturn(Optional.empty());
        AddressRequest.GetAddressByIdRequest req = AddressRequest.GetAddressByIdRequest.builder().addressId(invalidUUID).build();

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.GetAddressById(req);
        });

        assertEquals(ErrorStatus._ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(addressRepository, times(1)).findById(invalidUUID);
    }

    @DisplayName("존재하지 않는 주소의 경우 수정 실패")
    @Test
    void FAIL_updateAddress_not_found() {
        // 존재하지 않는 id
        UUID invalidUUID = UUID.randomUUID();
        AddressRequest.UpdateAddressRequest req = AddressRequest.UpdateAddressRequest.builder()
                .addressId(invalidUUID)
                .name("수정이름")
                .address("수정주소")
                .build();

        when(addressRepository.findById(invalidUUID)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.UpdateAddress(req);
        });

        assertEquals(ErrorStatus._ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(addressRepository, times(1)).findById(invalidUUID);
    }

    @DisplayName("존재하지 않는 주소의 경우 삭제 실패")
    @Test
    void FAIL_deleteAddress_not_found() {
        // 존재하지 않는 id
        UUID invalidUUID = UUID.randomUUID();
        when(addressRepository.findById(invalidUUID)).thenReturn(Optional.empty());
        AddressRequest.DeleteAddressRequest req = AddressRequest.DeleteAddressRequest.builder().addressId(invalidUUID).build();

        // When, Then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.DeleteAddress(req);
        });

        assertEquals(ErrorStatus._ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(addressRepository, times(1)).findById(invalidUUID);
    }

    //UserAddress
    @DisplayName("존재하지 않는 유저 배송지 추가 실패")
    @Test
    void FAIL_addUserAddress_user_not_found() {
        //유저 배송지
        UUID invalidUserId = UUID.randomUUID();
        AddressRequest.AddUserAddressRequest req = AddressRequest.AddUserAddressRequest.builder()
                .userAddressId(UUID.randomUUID())
                .userId(invalidUserId)
                .name(commonAddress.getName())
                .address(commonAddress.getAddress())
                .zipcode(commonAddress.getZipcode())
                .phone(commonAddress.getPhone())
                .addressDetail(commonAddress.getAddressDetail())
                .location(commonAddress.getLocation())
                .hubId(hub.getHubId())
                .build();

        //given
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        //when then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.AddUserAddress(req);
        });

        assertEquals(ErrorStatus._USER_NOT_FOUND, exception.getCode());
        verify(userRepository, times(1)).findById(invalidUserId);
    }
    @DisplayName("존재하지 않는 허브 배송지 추가 실패")
    @Test
    void FAIL_addUserAddress_hub_not_found() {
        //유저 배송지
        UUID invalidHubId = UUID.randomUUID();
        AddressRequest.AddUserAddressRequest req = AddressRequest.AddUserAddressRequest.builder()
                .userAddressId(UUID.randomUUID())
                .userId(user.getUserId())
                .name(commonAddress.getName())
                .address(commonAddress.getAddress())
                .zipcode(commonAddress.getZipcode())
                .phone(commonAddress.getPhone())
                .addressDetail(commonAddress.getAddressDetail())
                .location(commonAddress.getLocation())
                .hubId(invalidHubId)
                .build();

        //given
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(hubRepository.findById(invalidHubId)).thenReturn(Optional.empty());

        //when then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.AddUserAddress(req);
        });

        assertEquals(ErrorStatus._HUB_ID_NOT_FOUND, exception.getCode());
        verify(userRepository, times(1)).findById(user.getUserId());
        verify(hubRepository, times(1)).findById(invalidHubId);

    }
    @DisplayName("존재하지 않는 유저배송지의 허브 수정 실패")
    @Test
    void FAIL_updateUserAddressHub_not_found() {
        //유저 배송지 요청
        UUID invalidUserAddressId = UUID.randomUUID();
        AddressRequest.UpdateUserAddressHubRequest req = AddressRequest.UpdateUserAddressHubRequest.builder()
                .userAddressId(invalidUserAddressId)
                .hubId(hub.getHubId())
                .build();

        // given
        when(userAddressRepository.findById(invalidUserAddressId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.UpdateUserAddressHub(req);
        });

        assertEquals(ErrorStatus._USER_ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(userAddressRepository, times(1)).findById(invalidUserAddressId);
    }
    @DisplayName("존재하지 않는 허브로 허브 수정 실패")
    @Test
    void FAIL_updateUserAddressHub_hub_not_found() {
        // 유저 배송지 요청
        UUID userAddressId = userAddress.getUserAddressId();
        UUID invalidHubId = UUID.randomUUID();
        AddressRequest.UpdateUserAddressHubRequest req = AddressRequest.UpdateUserAddressHubRequest.builder()
                .userAddressId(userAddressId)
                .hubId(invalidHubId)
                .build();

        // given
        when(userAddressRepository.findById(userAddressId)).thenReturn(Optional.of(userAddress));
        when(hubRepository.findById(invalidHubId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.UpdateUserAddressHub(req);
        });

        assertEquals(ErrorStatus._HUB_ID_NOT_FOUND, exception.getCode());
        verify(userAddressRepository, times(1)).findById(userAddressId);
        verify(hubRepository, times(1)).findById(invalidHubId);
    }
    @DisplayName("존재하지 않는 유저 배송지 조회 실패")
    @Test
    void FAIL_getUserAddress_not_found() {
        //유저 배송지
        UUID invalidUserAddressId = UUID.randomUUID();
        AddressRequest.GetUserAddressRequest req = AddressRequest.GetUserAddressRequest.builder()
                .userAddressId(invalidUserAddressId)
                .build();

        //given
        when(userAddressRepository.findById(invalidUserAddressId)).thenReturn(Optional.empty());

        //when then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.GetUserAddress(req);
        });

        assertEquals(ErrorStatus._USER_ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(userAddressRepository, times(1)).findById(invalidUserAddressId);
    }
    @DisplayName("존재하지 않는 유저 배송지 리스트 조회 실패")
    @Test
    void FAIL_getAllUserAddressesById_user_not_found() {
        //유저 배송지
        UUID invalidUserId = UUID.randomUUID();
        AddressRequest.GetAllUserAddressesByIdRequest req = AddressRequest.GetAllUserAddressesByIdRequest.builder()
                .userId(invalidUserId)
                .build();

        //given
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        //when then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.GetAllUserAddressesById(req);
        });

        assertEquals(ErrorStatus._USER_NOT_FOUND, exception.getCode());
        verify(userRepository, times(1)).findById(invalidUserId);
    }
    @DisplayName("존재하지 않는 유저 배송지 삭제 실패")
    @Test
    void FAIL_deleteUserAddress_not_found() {
        //유저 배송지
        UUID invalidUserAddressId = UUID.randomUUID();
        AddressRequest.DeleteUserAddressRequest req = AddressRequest.DeleteUserAddressRequest.builder()
                .userAddressId(invalidUserAddressId)
                .build();

        //given
        when(userAddressRepository.findById(invalidUserAddressId)).thenReturn(Optional.empty());

        //when then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.DeleteUserAddress(req);
        });

        assertEquals(ErrorStatus._USER_ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(userAddressRepository, times(1)).findById(invalidUserAddressId);
    }
}
