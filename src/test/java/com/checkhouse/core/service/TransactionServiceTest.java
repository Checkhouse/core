package com.checkhouse.core.service;

import com.checkhouse.core.dto.request.TransactionRequest;
import com.checkhouse.core.dto.TransactionDTO;
import com.checkhouse.core.entity.*;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.entity.enums.Role;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.checkhouse.core.repository.mysql.TransactionRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.UserRepository;

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

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UsedProductRepository usedProductRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User seller;
    private User buyer;
    private OriginProduct originProduct1;
    private UsedProduct usedProduct1;
    private Transaction transaction1;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        seller = User.builder()
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .role(Role.ROLE_USER)
                .build();
        buyer = User.builder()
                .userId(UUID.randomUUID())
                .username("test user2")
                .email("test2@test.com")
                .nickname("test nickname2")
                .role(Role.ROLE_USER)
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
                usedProduct1,
                buyer
        );
        when(usedProductRepository.findById(usedProduct1.getUsedProductId()))
                .thenReturn(Optional.of(usedProduct1));
        when(transactionRepository.findByUsedProduct(usedProduct1.getUsedProductId()))
                .thenReturn(Optional.empty());
        when(userRepository.findById(buyer.getUserId()))
                .thenReturn(Optional.of(buyer));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction1);

        TransactionDTO result = transactionService.addTransaction(request);

        assertNotNull(result);
        assertEquals(transaction1.toDTO(), result);
    }

    @DisplayName("거래 상태 조회")
    @Test
    void SUCCESS_getTransactionStatus() {
        TransactionRequest.GetTransactionStatusRequest request = new TransactionRequest.GetTransactionStatusRequest(
                transaction1
        );

        when(transactionRepository.findById(transaction1.getTransactionId()))
                .thenReturn(Optional.of(transaction1));

        TransactionDTO result = transactionService.getTransactionStatus(request);

        assertNotNull(result);
        assertEquals(transaction1.toDTO(), result);
    }

    @DisplayName("거래 상태 변경")
    @Test
    void SUCCESS_updateTransactionStatus() {
        TransactionRequest.UpdateTransactionRequest request = new TransactionRequest.UpdateTransactionRequest(
                transaction1
        );

        when(transactionRepository.findById(transaction1.getTransactionId()))
                .thenReturn(Optional.of(transaction1));

        TransactionDTO result = transactionService.updateTransactionStatus(request);

        assertNotNull(result);
        assertEquals(transaction1.toDTO(), result);
    }

    @DisplayName("특정 사용자 거래 리스트 조회")
    @Test
    void SUCCESS_getTransactionsByUser() {
        TransactionRequest.GetTransactionsByUserRequest request = new TransactionRequest.GetTransactionsByUserRequest(
                buyer
        );

        when(userRepository.findById(buyer.getUserId()))
                .thenReturn(Optional.of(buyer));
        when(transactionRepository.findByBuyer(buyer.getUserId()))
                .thenReturn(List.of(transaction1));

        List<TransactionDTO> result = transactionService.getTransactionsByUser(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction1.toDTO(), result.get(0));
    }

    @DisplayName("관리자 거래 리스트 조회")
    @Test
    void SUCCESS_getTransactionsForAdmin() {
        User admin = User.builder()
                .userId(UUID.randomUUID())
                .username("admin")
                .email("admin@test.com")
                .nickname("admin nickname")
                .role(Role.ROLE_ADMIN)
                .build();
        TransactionRequest.GetTransactionsForAdminRequest request = new TransactionRequest.GetTransactionsForAdminRequest(
                admin
        );

        when(userRepository.findById(admin.getUserId()))
                .thenReturn(Optional.of(admin));
        when(transactionRepository.findAll())
                .thenReturn(List.of(transaction1));

        List<TransactionDTO> result = transactionService.getTransactionsForAdmin(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction1.toDTO(), result.get(0));
        verify(transactionRepository, times(1)).findAll();
    }

    @DisplayName("존재하지 않는 거래 정보 조회 실패")
    @Test
    void FAIL_getTransaction_not_found() {
        UUID invalidId = UUID.randomUUID();
        Transaction invalidTransaction = Transaction.builder()
                .transactionId(invalidId)
                .build();
        TransactionRequest.GetTransactionStatusRequest request = new TransactionRequest.GetTransactionStatusRequest(
                invalidTransaction
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
        TransactionRequest.AddTransactionRequest request = new TransactionRequest.AddTransactionRequest(
                usedProduct1,
                buyer
        );
        when(usedProductRepository.findById(usedProduct1.getUsedProductId()))
                .thenReturn(Optional.of(usedProduct1));
        when(transactionRepository.findByUsedProduct(usedProduct1.getUsedProductId()))
                .thenReturn(Optional.of(transaction1));

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.addTransaction(request));

        assertEquals(ErrorStatus._TRANSACTION_ALREADY_EXISTS, exception.getCode());
        verify(transactionRepository, times(1)).findByUsedProduct(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @DisplayName("존재하지 않는 중고 물품에 대한 거래 등록은 실패")
    @Test
    void FAIL_addTransaction_not_found() {
        UsedProduct invalidUsedProduct = UsedProduct.builder()
                .usedProductId(UUID.randomUUID())
                .build();
        TransactionRequest.AddTransactionRequest request = new TransactionRequest.AddTransactionRequest(
                invalidUsedProduct,
                buyer
        );

        when(usedProductRepository.findById(request.usedProduct().getUsedProductId()))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.addTransaction(request));

        assertEquals(ErrorStatus._USED_PRODUCT_NOT_FOUND, exception.getCode());
        verify(usedProductRepository, times(1)).findById(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @DisplayName("존재하지 않는 거래 상태는 변경 실패")
    @Test
    void FAIL_updateTransactionStatus_invalid_status() {
        Transaction invalidTransaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .build();
        TransactionRequest.UpdateTransactionRequest request = new TransactionRequest.UpdateTransactionRequest(
                invalidTransaction
        );

        when(transactionRepository.findById(request.transaction().getTransactionId()))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.updateTransactionStatus(request));

        assertEquals(ErrorStatus._TRANSACTION_NOT_FOUND, exception.getCode());
        verify(transactionRepository, times(1)).findById(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @DisplayName("사용자 특정 실패 시 리스트 조회 실패")
    @Test
    void FAIL_getTransactionsByUser_user_not_found() {
        User invalidUser = User.builder()
                .userId(UUID.randomUUID())
                .build();
        TransactionRequest.GetTransactionsByUserRequest request = new TransactionRequest.GetTransactionsByUserRequest(
                invalidUser
        );

        when(userRepository.findById(request.user().getUserId()))
                .thenReturn(Optional.empty());

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.getTransactionsByUser(request));

        assertEquals(ErrorStatus._TRANSACTION_USER_LIST_FAILED, exception.getCode());
        verify(userRepository, times(1)).findById(any());
    }

    @DisplayName("일반 유저가 관리자 거래 조회시 실패")
    @Test
    void FAIL_getTransactionsForAdmin_user_not_admin() {        
        TransactionRequest.GetTransactionsForAdminRequest request = new TransactionRequest.GetTransactionsForAdminRequest(
                buyer
        );

        when(userRepository.findById(request.admin().getUserId()))
                .thenReturn(Optional.of(buyer));

        GeneralException exception = assertThrows(GeneralException.class, () -> transactionService.getTransactionsForAdmin(request));

        assertEquals(ErrorStatus._USER_NOT_ADMIN, exception.getCode());
    }

}
