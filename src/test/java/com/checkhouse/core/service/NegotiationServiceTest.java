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
public class NegotiationServiceTest {
    @Mock
    private NegotiationRepository negotiationRepository;
    @Mock
    private UsedProductRepository usedProductRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NegotiationService negotiationService;

    private User seller;
    private User buyer;
    private OriginProduct originProduct1;
    private UsedProduct usedProduct1;
    private Negotiation negotiation1;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        seller = User.builder()
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
                .build();
        buyer = User.builder()
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
                .user(seller)
                .build();
        
        negotiation1 = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(2000)
                .state(NegotiationState.WAITING)
                .usedProduct(usedProduct1)
                .seller(seller)
                .buyer(buyer)
                .build();
    }

    @DisplayName("네고 생성")
    @Test
    void SUCCESS_addNegotiation() {
        NegotiationRequest.AddNegotiationRequest request = new NegotiationRequest.AddNegotiationRequest(
                usedProduct1.getUsedProductId(),
                seller.getUserId(),
                buyer.getUserId(),
                negotiation1.getPrice()
        );

        //given
        when(usedProductRepository.findById(usedProduct1.getUsedProductId()))
                .thenReturn(Optional.of(usedProduct1));
        when(userRepository.findById(seller.getUserId()))
                .thenReturn(Optional.of(seller));
        when(userRepository.findById(buyer.getUserId()))
                .thenReturn(Optional.of(buyer));
        when(negotiationRepository.save(any(Negotiation.class)))
                .thenReturn(negotiation1);

        //when
        NegotiationDTO result = negotiationService.addNegotiation(request);
        
        assertNotNull(result);
        assertEquals(negotiation1.toDto(), result);
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
        verify(negotiationRepository, never()).save(any(Negotiation.class));
    }

    @DisplayName("제안한 네고 내역 리스트 조회")
    @Test
    void SUCCESS_getProposedNegotiations() {
        NegotiationRequest.GetNegotiationByBuyerRequest request = new NegotiationRequest.GetNegotiationByBuyerRequest(
                buyer.getUserId()
        );

        //given
        when(negotiationRepository.findAllByBuyerId(buyer.getUserId()))
                .thenReturn(List.of(negotiation1));
        when(userRepository.findById(buyer.getUserId()))
                .thenReturn(Optional.of(buyer));

        //when
        List<NegotiationDTO> result = negotiationService.getNegotiationByBuyer(request);
        //
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(negotiation1.toDto(), result.get(0));
        verify(negotiationRepository, times(1)).findAllByBuyerId(buyer.getUserId());
    }

    @DisplayName("제안 받은 네고 내역 리스트 조회")
    @Test
    void SUCCESS_getReceivedNegotiations() {
        NegotiationRequest.GetNegotiationBySellerRequest request = new NegotiationRequest.GetNegotiationBySellerRequest(
                seller.getUserId()
        );
        //when
        when(negotiationRepository.findAllBySellerId(seller.getUserId()))
                .thenReturn(List.of(negotiation1));
        when(userRepository.findById(seller.getUserId()))
                .thenReturn(Optional.of(seller));

        //then
        List<NegotiationDTO> result = negotiationService.getNegotiationBySeller(request);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(negotiation1.toDto(), result.get(0));
        verify(negotiationRepository, times(1)).findAllBySellerId(seller.getUserId());
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
        assertEquals(NegotiationState.CANCELLED, result.state());
        verify(negotiationRepository, times(1)).findById(negotiation1.getNegotiationId());
        verify(negotiationRepository, never()).save(any(Negotiation.class));

    }

    @DisplayName("등록 가격보다 높거나 같은 가격의 경우 네고 등록 실패")
    @Test
    void FAIL_addNegotiation_invalid_price() {
        // 동일 가격 제안
        Negotiation samePriceNegotiation = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(3000)    // 같은 가격
                .state(NegotiationState.WAITING)
                .usedProduct(usedProduct1)
                .seller(seller)
                .build();
        
        NegotiationRequest.AddNegotiationRequest request = new NegotiationRequest.AddNegotiationRequest(
                samePriceNegotiation.getUsedProduct().getUsedProductId(),
                seller.getUserId(),
                buyer.getUserId(),
                samePriceNegotiation.getPrice() 
        );
        //given
        when(usedProductRepository.findById(samePriceNegotiation.getUsedProduct().getUsedProductId()))
                .thenReturn(Optional.of(samePriceNegotiation.getUsedProduct()));

        //when
        GeneralException exception = assertThrows(GeneralException.class,
                () -> negotiationService.addNegotiation(request));

        //then
        assertEquals(ErrorStatus._NEGOTIATION_PRICE_ERROR, exception.getCode());
        verify(usedProductRepository, times(1)).findById(any());
    }

    @DisplayName("존재하지 않는 네고 승인은 실패")
    @Test
    void FAIL_approveNegotiation_not_found() {  
        UUID invalidId = UUID.randomUUID();
        Negotiation invalidNegotiation = Negotiation.builder()
                .negotiationId(invalidId)
                .build();
        NegotiationRequest.UpdateNegotiationRequest request = new NegotiationRequest.UpdateNegotiationRequest(
                invalidNegotiation.getNegotiationId(),
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
        Negotiation AcceptedNegotiation = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(3000)
                .state(NegotiationState.ACCEPTED)
                .usedProduct(usedProduct1)
                .seller(seller)
                .build();

        NegotiationRequest.UpdateNegotiationRequest request = new NegotiationRequest.UpdateNegotiationRequest(
                AcceptedNegotiation.getNegotiationId(),
                NegotiationState.ACCEPTED
        );

        // given
        when(negotiationRepository.findById(AcceptedNegotiation.getNegotiationId()))
                .thenReturn(Optional.of(AcceptedNegotiation));

        // when
        GeneralException exception = assertThrows(GeneralException.class, () -> negotiationService.updateNegotiationState(request));

        // then
        assertEquals(ErrorStatus._NEGOTIATION_STATE_DUPLICATE, exception.getCode());
        verify(negotiationRepository, times(1)).findById(any());
        verify(negotiationRepository, never()).save(any(Negotiation.class));
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
                .user(seller)
                .build();
        
        NegotiationRequest.AddNegotiationRequest request = new NegotiationRequest.AddNegotiationRequest(
                deniedUsedProduct.getUsedProductId(),
                seller.getUserId(),
                buyer.getUserId(),
                2000
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
        UsedProduct invalidUsedProduct = UsedProduct.builder()
                .usedProductId(invalidUsedProductId)
                .build();
        Negotiation negotiation = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(2000)
                .state(NegotiationState.WAITING)
                .usedProduct(invalidUsedProduct)
                .seller(seller)
                .buyer(buyer)
                .build();
        
        NegotiationRequest.AddNegotiationRequest request = new NegotiationRequest.AddNegotiationRequest(
                invalidUsedProduct.getUsedProductId(),
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
                .seller(seller)
                .buyer(buyer)
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
        assertEquals(ErrorStatus._NEGOTIATION_ALREADY_CANCELLED, exception.getCode());
    }

    @DisplayName("네고 취소 시 제안이 이미 승인된 경우 실패")
    @Test
    void FAIL_cancelNegotiation_already_approved() {
        Negotiation approvedNegotiation = Negotiation.builder()
                .negotiationId(UUID.randomUUID())
                .price(2000)
                .state(NegotiationState.ACCEPTED)
                .usedProduct(usedProduct1)
                .seller(seller)
                .buyer(buyer)
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
        assertEquals(ErrorStatus._NEGOTIATION_ALREADY_ACCEPTED, exception.getCode());
    }

}
