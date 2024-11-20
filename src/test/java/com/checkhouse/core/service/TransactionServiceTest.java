package com.checkhouse.core.service;

import com.checkhouse.core.dto.request.TransactionRequest;
import com.checkhouse.core.dto.TransactionDTO;
import com.checkhouse.core.entity.*;
import com.checkhouse.core.entity.enums.UsedProductState;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.checkhouse.core.repository.mysql.TransactionRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    private UsedProductRepository usedProductRepository;

    @InjectMocks
    private User seller;
    private User buyer;
    private OriginProduct originProduct1;
    private UsedProduct usedProduct1;
    private Transaction transaction1;

    private TransactionService transactionService;


    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
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

        transaction1 = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .usedProduct(usedProduct1)
                .buyer(buyer)
                .isCompleted(false)
                .build();
    }

    @DisplayName("거래 생성")
    @Test
    void SUCCESS_addTransaction() {
        TransactionRequest.AddTransactionRequest request = new TransactionRequest.AddTransactionRequest(
                UUID.randomUUID(),
                usedProduct1.getUsedProductId(),
                buyer.getUserId()
        );

        TransactionDTO result = transactionService.addTransaction(request);

        assertNotNull(result);
        assertEquals(request.usedProductId(), result.usedProduct().getUsedProductId());
        assertEquals(request.buyerId(), result.buyer().getUserId());
    }

    @DisplayName("거래 상태 조회")
    @Test
    void SUCCESS_getTransactionStatus() {
        TransactionRequest.GetTransactionStatusRequest request = new TransactionRequest.GetTransactionStatusRequest(
                transaction1.getTransactionId()
        );

        when(transactionRepository.findById(transaction1.getTransactionId()))
                .thenReturn(Optional.of(transaction1));

        TransactionDTO result = transactionService.getTransactionStatus(request);

        assertNotNull(result);
        assertEquals(transaction1.getTransactionId(), result.transactionId());
        assertEquals(transaction1.getIsCompleted(), result.isCompleted());
    }

    @DisplayName("거래 상태 변경")
    @Test
    void SUCCESS_updateTransactionStatus() {
        TransactionRequest.UpdateTransactionRequest request = new TransactionRequest.UpdateTransactionRequest(
                transaction1.getTransactionId()
        );

        when(transactionRepository.findById(transaction1.getTransactionId()))
                .thenReturn(Optional.of(transaction1));

        TransactionDTO result = transactionService.updateTransactionStatus(request);

        assertNotNull(result);
        assertEquals(transaction1.getTransactionId(), result.transactionId());
    }

    @DisplayName("특정 사용자 거래 리스트 조회")
    @Test
    void SUCCESS_getTransactionsByUser() {
        TransactionRequest.GetTransactionsByUserRequest request = new TransactionRequest.GetTransactionsByUserRequest(
                buyer.getUserId()
        ); 

        when(transactionRepository.findByBuyer(buyer.getUserId()))
                .thenReturn(List.of(transaction1));

        List<TransactionDTO> result = transactionService.getTransactionsByUser(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction1.getTransactionId(), result.get(0).transactionId());
        assertEquals(transaction1.getIsCompleted(), result.get(0).isCompleted());
    }

    @DisplayName("관리자 거래 리스트 조회")
    @Test
    void SUCCESS_getTransactionsForAdmin() {}

    @DisplayName("존재하지 않는 거래 정보 조회 실패")
    @Test
    void FAIL_getTransaction_not_found() {
        UUID invalidId = UUID.randomUUID();
        TransactionRequest.GetTransactionStatusRequest request = new TransactionRequest.GetTransactionStatusRequest(
                invalidId
        );

        when(transactionRepository.findById(invalidId))
                .thenReturn(Optional.empty());  
    
        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.getTransactionStatus(request));
        
        assertEquals(ErrorStatus._TRANSACTION_NOT_FOUND, exception.getCode());
        verify(transactionRepository, times(1)).findById(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @DisplayName("같은 중고 상품에 대해 거래가 이미 생성된 경우 실패")
    @Test
    void FAIL_addTransaction_already_exists_error() {
        ///// 고쳐야 함

        UsedProduct usedProduct2 = UsedProduct.builder()
                .usedProductId(UUID.randomUUID())
                .originProduct(originProduct1)
                .title("[판매완료]아이패드")
                .description("싸다싸 너만오면 고")
                .price(3000)
                .isNegoAllow(true)
                .state(UsedProductState.POST_SALE)  // 판매완료
                .user(seller)
                .build();

        TransactionRequest.AddTransactionRequest request = new TransactionRequest.AddTransactionRequest(
                UUID.randomUUID(),  // 랜덤 거래
                usedProduct2.getUsedProductId(), // 중고 상품
                buyer.getUserId()
        );

        when(usedProductRepository.findById(usedProduct2.getUsedProductId()))
                .thenReturn(Optional.of(usedProduct2));

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.addTransaction(request));
        
        assertEquals(ErrorStatus._USED_PRODUCT_NOT_FOUND, exception.getCode());
        verify(usedProductRepository, times(1)).findById(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @DisplayName("존재하지 않는 중고 물품에 대한 거래 등록은 실패")
    @Test
    void FAIL_addTransaction_not_found() {
        TransactionRequest.AddTransactionRequest request = new TransactionRequest.AddTransactionRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                buyer.getUserId()
        );

        when(usedProductRepository.findById(request.usedProductId()))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.addTransaction(request));

        assertEquals(ErrorStatus._USED_PRODUCT_NOT_FOUND, exception.getCode());
        verify(usedProductRepository, times(1)).findById(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @DisplayName("존재하지 않는 거래 상태는 변경 실패")
    @Test
    void FAIL_updateTransactionStatus_invalid_status() {
        TransactionRequest.UpdateTransactionRequest request = new TransactionRequest.UpdateTransactionRequest(
                UUID.randomUUID()
        );

        when(transactionRepository.findById(request.transactionId()))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.updateTransactionStatus(request));

        assertEquals(ErrorStatus._TRANSACTION_NOT_FOUND, exception.getCode());
        verify(transactionRepository, times(1)).findById(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @DisplayName("사용자 특정 실패 시 리스트 조회 실패")
    @Test
    void FAIL_getTransactionsByUser_user_not_found() {
        TransactionRequest.GetTransactionsByUserRequest request = new TransactionRequest.GetTransactionsByUserRequest(
                UUID.randomUUID()
        );

        when(transactionRepository.findByBuyer(request.userId()))
                .thenReturn(List.of());

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.getTransactionsByUser(request));

        assertEquals(ErrorStatus._TRANSACTION_USER_LIST_FAILED, exception.getCode());
        verify(transactionRepository, times(1)).findByBuyer(any());
    }

}
