package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.NegotiationDTO;
import com.checkhouse.core.entity.*;
import com.checkhouse.core.entity.enums.NegotiationState;
import com.checkhouse.core.repository.mysql.NegotiationRepository;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.dto.request.NegotiationRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class NegotiationServiceTest {
    @Mock
    private NegotiationRepository negotiationRepository;
    private UsedProductRepository usedProductRepository;

    @InjectMocks
    private User user1;
    private User user2;
    private OriginProduct originProduct1;
    private UsedProduct usedProduct1;
    private Negotiation negotiation1;

    private NegotiationService negotiationService;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        user1 = User.builder()
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
                .build();
        user2 = User.builder()
                .userId(UUID.randomUUID())
                .username("test user2")
                .email("test2@test.com")
                .nickname("test nickname2")
                .password(null)
                .role(Role.ROLE_USER)   
                .provider("naver")
                .providerId("random id naver")
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
                .user(user1)
                .build();
        
        negotiation1 = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(2000)
                .state(NegotiationState.WAITING)
                .usedProduct(usedProduct1)
                .seller(user1)
                .build();
    }

    @DisplayName("네고 생성")
    @Test
    void SUCCESS_addNegotiation() {
        NegotiationRequest.AddNegotiationRequest request = new NegotiationRequest.AddNegotiationRequest(
                UUID.randomUUID(),
                usedProduct1.getUsedProductId(),
                user1.getUserId(),
                user1.getUserId(),
                3000
        );

        NegotiationDTO result = negotiationService.addNegotiation(request);

        assertNotNull(result);
        assertEquals(request.price(), result.price());
        assertEquals(request.usedProductId(), result.usedProduct().getUsedProductId());
        assertEquals(request.buyerId(), result.buyer().getUserId());
        assertEquals(request.sellerId(), result.seller().getUserId());
    }

    @DisplayName("네고 승인 - 수락 or 거절")
    @Test
    void SUCCESS_approveNegotiation() {
        // 요청
        NegotiationRequest.UpdateNegotiationRequest acceptedRequest = new NegotiationRequest.UpdateNegotiationRequest(
                negotiation1.getNegotiationId(),
                NegotiationState.ACCEPTED       // 수락
        );

        //given
        when(negotiationRepository.findById(negotiation1.getNegotiationId()))
                .thenReturn(Optional.of(negotiation1));

        //when
        NegotiationDTO result = negotiationService.updateNegotiationState(acceptedRequest);

        //then
        assertNotNull(result);
        assertEquals(NegotiationState.ACCEPTED, result.state());
        verify(negotiationRepository, times(1)).findById(negotiation1.getNegotiationId());
        verify(negotiationRepository, times(1)).save(any(Negotiation.class));
    }

    @DisplayName("제안한 네고 내역 리스트 조회")
    @Test
    void SUCCESS_getProposedNegotiations() {
        NegotiationRequest.GetNegotiationByBuyerRequest request = new NegotiationRequest.GetNegotiationByBuyerRequest(
                user1.getUserId()
        );

        //given
        when(negotiationRepository.findAllByBuyerId(user1.getUserId()))
                .thenReturn(List.of(negotiation1));

        //when
        List<NegotiationDTO> result = negotiationService.getNegotiationByBuyer(request);
        //
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(negotiation1.getNegotiationId(), result.get(0).negotiationId());
        verify(negotiationRepository, times(1)).findAllByBuyerId(user1.getUserId());
    }

    @DisplayName("제안 받은 네고 내역 리스트 조회")
    @Test
    void SUCCESS_getReceivedNegotiations() {
        NegotiationRequest.GetNegotiationBySellerRequest request = new NegotiationRequest.GetNegotiationBySellerRequest(
                user1.getUserId()
        );
        //when
        when(negotiationRepository.findAllBySellerId(user1.getUserId()))
                .thenReturn(List.of(negotiation1));

        //then
        List<NegotiationDTO> result = negotiationService.getNegotiationBySeller(request);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(negotiation1.getNegotiationId(), result.get(0).negotiationId());
        verify(negotiationRepository, times(1)).findAllByBuyerId(user1.getUserId());
    }

    @DisplayName("네고 취소")
    @Test
    void SUCCESS_cancelNegotiation() {
                // 요청
        NegotiationRequest.UpdateNegotiationRequest cancelledRequest = new NegotiationRequest.UpdateNegotiationRequest(
                negotiation1.getNegotiationId(),
                NegotiationState.CANCELLED       // 취소
        );

        //given
        when(negotiationRepository.findById(negotiation1.getNegotiationId()))
                .thenReturn(Optional.of(negotiation1));

        //when
        NegotiationDTO result = negotiationService.updateNegotiationState(cancelledRequest);

        //then
        assertNotNull(result);
        assertEquals(NegotiationState.ACCEPTED, result.state());
        verify(negotiationRepository, times(1)).findById(negotiation1.getNegotiationId());
        verify(negotiationRepository, times(1)).save(any(Negotiation.class));

    }

    @DisplayName("등록 가격보다 높거나 같은 가격의 경우 네고 등록 실패")
    @Test
    void FAIL_addNegotiation_invalid_price() {
        // 동일 가격 제안
        Negotiation samePriceNegotiation = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(3000)
                .state(NegotiationState.WAITING)
                .usedProduct(usedProduct1)
                .seller(user1)
                .build();
        
        NegotiationRequest.AddNegotiationRequest request = new NegotiationRequest.AddNegotiationRequest(
                samePriceNegotiation.getNegotiationId(),
                samePriceNegotiation.getUsedProduct().getUsedProductId(),
                samePriceNegotiation.getSeller().getUserId(),
                samePriceNegotiation.getBuyer().getUserId(),
                samePriceNegotiation.getPrice() 
        );
        //given
        when(negotiationRepository.save(any(Negotiation.class)))
                .thenReturn(samePriceNegotiation);

        //when
        GeneralException exception = assertThrows(GeneralException.class,
                () -> negotiationService.addNegotiation(request));

        //then
        assertEquals(ErrorStatus._NEGOTIATION_PRICE_ERROR, exception.getCode());
        verify(negotiationRepository, times(1)).save(any(Negotiation.class));
    }

    @DisplayName("존재하지 않는 네고 승인은 실패")
    @Test
    void FAIL_approveNegotiation_not_found() {  
        UUID invalidId = UUID.randomUUID();
        NegotiationRequest.UpdateNegotiationRequest request = new NegotiationRequest.UpdateNegotiationRequest(
                invalidId,
                NegotiationState.ACCEPTED
        );

        // given
        when(negotiationRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when
        GeneralException exception = assertThrows(GeneralException.class, () -> negotiationService.updateNegotiationState(request));

        // then
        assertEquals(ErrorStatus._NEGOTIATION_NOT_FOUND, exception.getCode());
        verify(negotiationRepository, times(1)).findById(any());
        verify(negotiationRepository, never()).save(any(Negotiation.class));
    }

    @DisplayName("이미 승인된 네고에 대한 승인은 실패")
    @Test
    void FAIL_approveNegotiation_already_approved() {  
        UUID invalidId = UUID.randomUUID();
        NegotiationRequest.UpdateNegotiationRequest request = new NegotiationRequest.UpdateNegotiationRequest(
                invalidId,
                NegotiationState.ACCEPTED
        );

        // given
        when(negotiationRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when
        GeneralException exception = assertThrows(GeneralException.class, () -> negotiationService.updateNegotiationState(request));

        // then
        assertEquals(ErrorStatus._NEGOTIATION_NOT_FOUND, exception.getCode());
        verify(negotiationRepository, times(1)).findById(any());
        verify(negotiationRepository, never()).save(any(Negotiation.class));
    }

    @DisplayName("이미 거절된 네고에 대한 거절은 실패")
    @Test
    void FAIL_approveNegotiation_already_denied() {
        // ?
    }

    @DisplayName("네고를 거부한 중고 상품에 대한 내고 생성은 실패")
    @Test
    void FAIL_addNegotiation_nego_is_not_allowed() {
        UsedProduct deniedUsedProduct = UsedProduct.builder()
                .usedProductId(UUID.randomUUID())
                .title("아이패드 떨이")
                .description("싸다싸 너만오면 고")
                .price(3000)
                .isNegoAllow(false)             // 네고 거부
                .state(UsedProductState.ON_SALE)
                .originProduct(originProduct1)
                .user(user1)
                .build();
        
        NegotiationRequest.AddNegotiationRequest request = new NegotiationRequest.AddNegotiationRequest(
                UUID.randomUUID(),
                deniedUsedProduct.getUsedProductId(),
                user1.getUserId(),
                user2.getUserId(),
                3000
        );      

        //given
        when(usedProductRepository.findById(deniedUsedProduct.getUsedProductId()))
                .thenReturn(Optional.of(deniedUsedProduct));

        //when
        GeneralException exception = assertThrows(GeneralException.class,
                () -> negotiationService.addNegotiation(request));

        //then
        assertEquals(ErrorStatus._NEGOTIATION_NOT_ALLOWED, exception.getCode());
        verify(usedProductRepository, times(1)).findById(any());
    }

    @DisplayName("중고상품이 없는 경우 가격 제안 실패")
    @Test
    void FAIL_addNegotiation_no_used_product() {
        // given
        UUID invalidUsedProductId = UUID.randomUUID();
        Negotiation negotiation = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(2000)
                .state(NegotiationState.WAITING)
                .usedProduct(null)
                .seller(user1)
                .buyer(user2)
                .build();
        
        NegotiationRequest.AddNegotiationRequest request = new NegotiationRequest.AddNegotiationRequest(
                negotiation.getNegotiationId(),
                invalidUsedProductId,                   // 존재하지 않는 중고상품 ID
                negotiation.getSeller().getUserId(),
                negotiation.getBuyer().getUserId(),
                negotiation.getPrice()
        );
        when(usedProductRepository.findById(invalidUsedProductId))
                .thenReturn(Optional.empty());

        // when
        GeneralException exception = assertThrows(GeneralException.class,
                () -> negotiationService.addNegotiation(request));

        // then
        assertEquals(ErrorStatus._USED_PRODUCT_NOT_FOUND, exception.getCode());
        
        // 중고상품 조회만 실행되고, 이후 로직은 실행되지 않아야 함
        verify(usedProductRepository, times(1)).findById(any());
    }

    @DisplayName("네고 승인 시에 제안이 이미 취소된 경우 실패")
    @Test
    void FAIL_approveNegotiation_already_cancelled() {
        Negotiation cancelledNegotiation = Negotiation.builder()        
                .negotiationId(UUID.randomUUID())
                .price(2000)
                .state(NegotiationState.CANCELLED)       // 취소
                .usedProduct(usedProduct1)
                .seller(user1)
                .buyer(user2)
                .build();

        NegotiationRequest.UpdateNegotiationRequest request = new NegotiationRequest.UpdateNegotiationRequest(
                cancelledNegotiation.getNegotiationId(),
                NegotiationState.ACCEPTED
        );

        // given
        when(negotiationRepository.findById(cancelledNegotiation.getNegotiationId()))
                .thenReturn(Optional.of(cancelledNegotiation));

        // when
        GeneralException exception = assertThrows(GeneralException.class, () -> negotiationService.updateNegotiationState(request));

        // then 
        assertEquals(ErrorStatus._NEGOTIATION_NOT_FOUND, exception.getCode());
    }

    @DisplayName("네고 취소 시 제안이 이미 승인된 경우 실패")
    @Test
    void FAIL_cancelNegotiation_already_approved() {
        Negotiation approvedNegotiation = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(2000)
                .state(NegotiationState.ACCEPTED)
                .usedProduct(usedProduct1)
                .seller(user1)
                .buyer(user2)
                .build();

        NegotiationRequest.UpdateNegotiationRequest request = new NegotiationRequest.UpdateNegotiationRequest(
                approvedNegotiation.getNegotiationId(),
                NegotiationState.CANCELLED
        );

        // given
        when(negotiationRepository.findById(approvedNegotiation.getNegotiationId()))
                .thenReturn(Optional.of(approvedNegotiation));

        // when
        GeneralException exception = assertThrows(GeneralException.class, () -> negotiationService.updateNegotiationState(request));

        // then
        assertEquals(ErrorStatus._NEGOTIATION_NOT_FOUND, exception.getCode());
    }

}
