package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.dto.request.AddressRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    @InjectMocks
    private AddressService addressService;

    private Address commonAddress;
    private Address otherAddress;

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
                .build();
        otherAddress = Address.builder()
                .addressId(UUID.randomUUID())
                .name("이름입니다")
                .address("서울특별시 동작구 상도1동")
                .zipcode(12345)
                .phone("01023456789")
                .addressDetail("2220호")
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

        AddressDTO result = addressService.addAddress(req);
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

        AddressDTO result = addressService.updateAddress(req);

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

        AddressDTO result = addressService.updateAddress(req);

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
        addressService.deleteAddress(req);

        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).delete(any(Address.class));
    }

    @DisplayName("주소 조회 성공")
    @Test
    void SUCCESS_getAddress() {
        UUID addressId = commonAddress.getAddressId();
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(commonAddress));

        AddressRequest.GetAddressByIdRequest req = AddressRequest.GetAddressByIdRequest.builder().addressId(addressId).build();
        AddressDTO result = addressService.getAddressById(req);

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

        List<AddressDTO> result = addressService.getAllAddresses();


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


    @DisplayName("존재하지 않는 주소의 경우 조회 실패")
    @Test
    void FAIL_getAddress_not_found() {
        //존재하지 않는 id
        UUID invalidUUID = UUID.randomUUID();
        when(addressRepository.findById(invalidUUID)).thenReturn(Optional.empty());
        AddressRequest.GetAddressByIdRequest req = AddressRequest.GetAddressByIdRequest.builder().addressId(invalidUUID).build();

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            addressService.getAddressById(req);
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
            addressService.updateAddress(req);
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
            addressService.deleteAddress(req);
        });

        assertEquals(ErrorStatus._ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(addressRepository, times(1)).findById(invalidUUID);
    }

    //TODO: soft delete testcode
    @DisplayName("삭제된 주소의 경우 조회 실패")
    @Test
    void FAIL_getAddress_deleted_address() {
    }

    @DisplayName("삭제된 주소의 경우 수정 실패")
    @Test
    void FAIL_updateAddress_deleted_address() {

    }

    @DisplayName("삭제된 주소의 경우 삭제 실패")
    @Test
    void FAIL_deleteAddress_deleted_address() {}
}
