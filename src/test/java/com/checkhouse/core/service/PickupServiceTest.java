package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.entity.Pickup;
import com.checkhouse.core.entity.Transaction;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Store;
import com.checkhouse.core.entity.enums.UsedProductState;

import com.checkhouse.core.repository.mysql.*;

import com.checkhouse.core.dto.PickupDTO;
import com.checkhouse.core.dto.request.PickupRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PickupServiceTest {
    @Mock
    private PickupRepository pickupRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PickupService pickupService;

    private User seller;
    private User buyer;
    private OriginProduct originProduct1;
    private UsedProduct usedProduct1;
    private Transaction transaction1;
    private Address store1addr;
    private Store store1;
    private Pickup pickup1;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        // 유저 셋업
        seller = User.builder()
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .build();
        buyer = User.builder()
                .userId(UUID.randomUUID())
                .username("test user2")
                .email("test2@test.com")
                .nickname("test nickname2")
                .build();

        // 중고 상품 셋업
        originProduct1 = OriginProduct.builder()
                .id(UUID.randomUUID())
                .name("아이패드")
                .company("애플")
                .category(
                        Category.builder()
                                .name("category1")
                                .build()
                )
                .build();
        usedProduct1 = UsedProduct.builder()
                .usedProductId(UUID.randomUUID())
                .title("아이패드 떨이")
                .description("싸다싸 너만오면 고")
                .price(3000)
                .isNegoAllow(true)
                .state(UsedProductState.ON_SALE)
                .originProduct(originProduct1)
                .user(seller)
                .build();

        // 거래 셋업
        transaction1 = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .usedProduct(usedProduct1)
                .buyer(buyer)
                .isCompleted(false)
                .build();

        // 스토어 셋업
        store1addr = Address.builder()
                .addressId(UUID.randomUUID())
                .name("홍길동")
                .address("서울특별시 동작구 상도로 369")
                .zipcode(6978)
                .phone("01012345678")
                .addressDetail("정보과학관 지하 1층")
                .build();
        store1 = Store.builder()
                .storeId(UUID.randomUUID())
                .name("정보섬티맥스")
                .code("123456")
                .address(store1addr)
                .build();

        // 픽업 셋업
        pickup1 = Pickup.builder()
                .pickupId(UUID.randomUUID())
                .transaction(transaction1)
                .store(store1)
                .isPicked_up(false)
                .build();
    }

    @DisplayName("픽업 생성")
    @Test
    void SUCCESS_addUserPickupList() {
        PickupRequest.AddPickUpRequest addPickUpRequest = new PickupRequest.AddPickUpRequest(
                transaction1.getTransactionId(),
                store1.getStoreId()
        );

        when(transactionRepository.findById(transaction1.getTransactionId()))
                .thenReturn(Optional.of(transaction1));
        when(userRepository.findById(buyer.getUserId()))
                .thenReturn(Optional.of(buyer));
        when(storeRepository.findById(store1.getStoreId()))
                .thenReturn(Optional.of(store1));
        when(pickupRepository.save(any(Pickup.class)))
                .thenReturn(pickup1);

        PickupDTO result = pickupService.addUserPickup(addPickUpRequest);

        assertNotNull(result);
        assertEquals(pickup1.toDto(), result);
        verify(pickupRepository, times(1)).save(any(Pickup.class));
        verify(transactionRepository, times(1)).findById(transaction1.getTransactionId());
        verify(userRepository, times(1)).findById(buyer.getUserId());
        verify(storeRepository, times(1)).findById(store1.getStoreId());
    }

    @DisplayName("사용자의 픽업 리스트 조회 성공")
    @Test
    void SUCCESS_getUserPickupList() {
        PickupRequest.GetUserPickupListRequest getUserPickupListRequest = new PickupRequest.GetUserPickupListRequest(
                buyer.getUserId()
        );

        when(userRepository.findById(buyer.getUserId()))
                .thenReturn(Optional.of(buyer));
        when(pickupRepository.findByUserId(buyer.getUserId()))
                .thenReturn(List.of(pickup1));

        List<PickupDTO> result = pickupService.getUserPickupList(getUserPickupListRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pickup1.toDto(), result.get(0));
        verify(pickupRepository, times(1)).findByUserId(buyer.getUserId());
    }

    @DisplayName("사용자의 특정 픽업 정보 조회 성공")
    @Test
    void SUCCESS_getUserPickupDetails() {
        PickupRequest.GetUserPickupDetailsRequest getUserPickupDetailsRequest = new PickupRequest.GetUserPickupDetailsRequest(
                pickup1.getPickupId(),
                buyer.getUserId()
        );

        when(userRepository.findById(buyer.getUserId()))
                .thenReturn(Optional.of(buyer));
        when(pickupRepository.findById(pickup1.getPickupId()))
                .thenReturn(Optional.of(pickup1));

        PickupDTO result = pickupService.getUserPickupDetails(getUserPickupDetailsRequest);

        assertNotNull(result);
        assertEquals(pickup1.toDto(), result);
        verify(userRepository, times(1)).findById(buyer.getUserId());
        verify(pickupRepository, times(1)).findById(pickup1.getPickupId());
    }

    // toDo 관리자 관련
    @DisplayName("픽업 확인 - 스토어 관리자가 qr 찍은 후")
    @Test
    void SUCCESS_verifyPickupWithQR() {
        // qr 서비스 추가
    }

//    @DisplayName("픽업 확인 - 관리자 id 입력해서")
//    @Test
//    void SUCCESS_verifyPickupWithAdminId() {
//        // 관리자 id가 맞는지
//        PickupRequest.UpdatePickUpForAdminRequest updatePickUpForAdminRequest = new PickupRequest.UpdatePickUpForAdminRequest(
//                pickup1.getPickupId(),
//                store1.getStoreId(),
//                "123456"
//        );
//
//        when(storeRepository.findById(store1.getStoreId()))
//                .thenReturn(Optional.of(store1));
//        when(pickupRepository.findById(pickup1.getPickupId()))
//                .thenReturn(Optional.of(pickup1));
//        when(transactionRepository.findById(transaction1.getTransactionId()))
//                .thenReturn(Optional.of(transaction1));
//
//        PickupDTO result = pickupService.updatePickupForAdmin(updatePickUpForAdminRequest);
//
//        assertNotNull(result);
//        assertEquals(pickup1.toDto(), result);
//    }

    @DisplayName("특정 스토어의 픽업 리스트 조회 성공")
    @Test
    void SUCCESS_getStorePickupListForAdmin() {

    }


    @DisplayName("존재하지 않는 거래에 대한 픽업 생성 실패")
    @Test
    void FAIL_addUserPickupList_transaction_not_found() {
        UUID invalidTransactionId = UUID.randomUUID();
        Transaction invalidTransaction = Transaction.builder()
                .transactionId(invalidTransactionId)
                .build();

        PickupRequest.AddPickUpRequest addPickUpRequest = new PickupRequest.AddPickUpRequest(
                invalidTransaction.getTransactionId(),
                store1.getStoreId()
        );

        when(transactionRepository.findById(invalidTransactionId))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> pickupService.addUserPickup(addPickUpRequest));
        assertEquals(ErrorStatus._TRANSACTION_NOT_FOUND, exception.getCode());
    }
    @DisplayName("존재하지 않는 스토어에 대한 픽업 생성 실패")
    @Test
    void FAIL_addUserPickupList_store_not_found() {
        UUID invalidStoreId = UUID.randomUUID();
        Store invalidStore = Store.builder()
                .storeId(invalidStoreId)
                .build();

        PickupRequest.AddPickUpRequest addPickUpRequest = new PickupRequest.AddPickUpRequest(
                transaction1.getTransactionId(),
                invalidStore.getStoreId()
        );

        when(transactionRepository.findById(transaction1.getTransactionId()))
                .thenReturn(Optional.of(transaction1));
        when(userRepository.findById(buyer.getUserId()))
                .thenReturn(Optional.of(buyer));
        when(storeRepository.findById(invalidStoreId))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> pickupService.addUserPickup(addPickUpRequest));
        
        assertEquals(ErrorStatus._PICKUP_STORE_NOT_FOUND, exception.getCode());
        verify(storeRepository, times(1)).findById(invalidStoreId);
    }
    @DisplayName("존재하지 않는 사용자의 경우 픽업 리스트 조회 실패")
    @Test
    void FAIL_getUserPickupList_user_not_found() {
        UUID invalidUserId = UUID.randomUUID();
        User invalidUser = User.builder()
                .userId(invalidUserId)
                .build();

        PickupRequest.GetUserPickupListRequest getUserPickupListRequest = new PickupRequest.GetUserPickupListRequest(
                invalidUser.getUserId()
        );

        when(userRepository.findById(invalidUserId))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> pickupService.getUserPickupList(getUserPickupListRequest));
        
        assertEquals(ErrorStatus._PICKUP_USER_NOT_FOUND, exception.getCode());
        verify(userRepository, times(1)).findById(invalidUserId);
    }
    @DisplayName("존재하지 않는 픽업의 경우 조회 실패")
    @Test
    void FAIL_getPickupDetails_not_found() {
        UUID invalidPickupId = UUID.randomUUID();
        Pickup invalidPickup = Pickup.builder()
                .pickupId(invalidPickupId)
                .build();

        PickupRequest.GetUserPickupDetailsRequest getUserPickupDetailsRequest = new PickupRequest.GetUserPickupDetailsRequest(
                invalidPickup.getPickupId(),
                seller.getUserId()
        );

        when(pickupRepository.findById(invalidPickupId))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> pickupService.getUserPickupDetails(getUserPickupDetailsRequest));
        
        assertEquals(ErrorStatus._PICKUP_NOT_FOUND, exception.getCode());
        verify(pickupRepository, times(1)).findById(invalidPickupId);
    }

    //toDo 관리자 관련
    @DisplayName("존재하지 않는 픽업의 경우 픽업 확인 실패")
    @Test
    void FAIL_verifyPickup_not_found() {
        // 확인 누르고 나서 바뀐 경우에 대한 테스트
    }

    @DisplayName("픽업 확인 시 관리자 코드가 다른 경우 픽업 확인 실패")
    @Test
    void FAIL_verifyPickup_invalid_code() {

    }

    @DisplayName("존재하지 않는 스토어의 경우 픽업 리스트 조회 실패")
    @Test
    void FAIL_getStorePickupList_store_not_found() {

    }

}
