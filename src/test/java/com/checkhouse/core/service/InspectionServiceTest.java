package com.checkhouse.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.InspectionDTO;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.InspectionRequest;
import com.checkhouse.core.dto.request.InspectionRequest.AddInspectionRequest;
import com.checkhouse.core.dto.request.InspectionRequest.UpdateInspectionRequest;
import com.checkhouse.core.entity.Inspection;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.repository.mysql.InspectionRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.checkhouse.core.entity.OriginProduct;

@ExtendWith(MockitoExtension.class)
public class InspectionServiceTest {
    @Mock
    private InspectionRepository inspectionRepository;
    @Mock
    private UsedProductRepository usedProductRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private InspectionService inspectionService;
    @Mock
    private OriginProductRepository originProductRepository;
    
    

    private static Inspection inspection1;
    private static User user1;
    private static UsedProduct usedProduct1;
    private static OriginProduct originProduct;

    @BeforeEach
    void setup() {
        originProduct = OriginProduct.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .build();

        user1 = User.builder()
                .userId(UUID.randomUUID())
                .nickname("testUser")
                .email("test@test.com")
                .password("testPassword")
                .provider("testProvider")
                .providerId("testProviderId")
                .isActive(true)
                .build();

        usedProduct1 = UsedProduct.builder()
                .usedProductId(UUID.randomUUID())
                .user(user1)
                .originProduct(originProduct)
                .build();

        inspection1 = Inspection.builder()
                .inspectionId(UUID.randomUUID())
                .isDone(false)
                .description("test description")
                .usedProduct(usedProduct1)
                .user(user1)
                .build();
    }

    @DisplayName("검수 등록 성공")
    @Test
    void SUCCESS_addInspection() {
        // given
        AddInspectionRequest req = AddInspectionRequest.builder()
                .usedProductId(usedProduct1.getUsedProductId())
                .description("test description")
                .isDone(false)
                .userId(user1.getUserId())
                .build();

        when(userRepository.findById(user1.getUserId())).thenReturn(Optional.of(user1));
        when(usedProductRepository.findById(usedProduct1.getUsedProductId())).thenReturn(Optional.of(usedProduct1));
        when(inspectionRepository.save(any(Inspection.class))).thenReturn(inspection1);

        // when
        InspectionDTO response = inspectionService.addInspection(req);

        // then
        assertEquals(inspection1.getInspectionId(), response.inspectionId());
        verify(inspectionRepository).save(any(Inspection.class));
    }
    @DisplayName("검수 사진 등록 - 상태 업데이트")
    @Test
    void SUCCESS_addInspectionPhoto_updateStatus() {
        //데이터 등록
        //todo: 사진 등록 어떤식으로 넣어야 하는지

    }

    @DisplayName("검수 리스트 조회 - 관리자")
    @Test
    void SUCCESS_getInspectionListForAdmin() {
        // given
        List<Inspection> inspectionList = List.of(inspection1);
        UUID usedProductId = usedProduct1.getUsedProductId();
        
        when(inspectionRepository.findByUsedProduct_UsedProductId(usedProductId))
            .thenReturn(inspectionList);

        // when
        List<InspectionDTO> response = inspectionService.getInspectionList(usedProductId);

        // then
        assertEquals(1, response.size());
        assertEquals(inspection1.getInspectionId(), response.get(0).inspectionId());
        assertEquals(inspection1.getDescription(), response.get(0).description());
        verify(inspectionRepository).findByUsedProduct_UsedProductId(usedProductId);
    }

    @DisplayName("판매 등록이 안된 상품일 때 실패")
    @Test
    void FAIL_addInspectionItem_not_registered_for_sale() {
        // given
        AddInspectionRequest req = AddInspectionRequest.builder()
                .usedProductId(null)
                .description("test description")
                .isDone(false)
                .userId(user1.getUserId())
                .build();

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, 
            () -> inspectionService.addInspection(req));
        assertEquals(ErrorStatus._USED_PRODUCT_ID_NOT_FOUND.getCode(), exception.getErrorReason().getCode());
    }

    @DisplayName("존재하지 않는 사용자로 검수 등록 시 실패")
    @Test
    void FAIL_addInspection_user_not_found() {
        // given
        UUID nonExistentUserId = UUID.randomUUID();
        
        AddInspectionRequest req = AddInspectionRequest.builder()
                .usedProductId(usedProduct1.getUsedProductId())
                .description("test description")
                .isDone(false)
                .userId(nonExistentUserId)
                .build();

        // when & then
        assertThrows(GeneralException.class, () -> inspectionService.addInspection(req));
    }
    @DisplayName("이미 완료된 검수의 경우 검수 등록 실패")
    @Test
    void FAIL_addInspectionItem_already_done() {
        // given
        AddInspectionRequest req = AddInspectionRequest.builder()
                .usedProductId(usedProduct1.getUsedProductId())
                .description("new inspection")
                .isDone(false)
                .userId(user1.getUserId())
                .build();

        when(usedProductRepository.findById(usedProduct1.getUsedProductId()))
            .thenReturn(Optional.of(usedProduct1));
        when(inspectionRepository.findByUsedProductAndIsDone(usedProduct1, true))
            .thenReturn(Optional.of(inspection1));  // 이미 완료된 검수가 있다고 가정

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, 
            () -> inspectionService.addInspection(req));
        assertEquals("INSPECTION400", exception.getErrorReason().getCode());
    }

    @DisplayName("이미 완료된 검수의 경우 검수 상태 변경 실패")
    @Test
    void FAIL_updateInspectionStatus_already_done() {
        // given
        Inspection completedInspection = Inspection.builder()
                .inspectionId(UUID.randomUUID())
                .isDone(true)
                .description("test description")
                .usedProduct(usedProduct1)
                .user(user1)
                .build();
        
        UpdateInspectionRequest req = UpdateInspectionRequest.builder()
                .inspectionId(completedInspection.getInspectionId())
                .isDone(true)
                .build();

        when(inspectionRepository.findById(completedInspection.getInspectionId()))
            .thenReturn(Optional.of(completedInspection));

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, 
            () -> inspectionService.updateInspection(completedInspection.getInspectionId(), req));
        assertEquals(ErrorStatus._INSPECTION_ALREADY_DONE.getCode(), exception.getErrorReason().getCode());
    }

    @DisplayName("이미 검수가 존재하는 상품은 검수 등록 실패 (완료되지 않은 상태라도)")
    @Test
    void FAIL_addInspection_already_exists() {
        // given
        Inspection existingInspection = Inspection.builder()
                .inspectionId(UUID.randomUUID())
                .isDone(false)  // 완료되지 않은 상태라도
                .description("existing inspection")
                .usedProduct(usedProduct1)
                .user(user1)
                .build();

        AddInspectionRequest req = AddInspectionRequest.builder()
                .usedProductId(usedProduct1.getUsedProductId())
                .description("new inspection")
                .isDone(false)
                .userId(user1.getUserId())
                .build();

        when(usedProductRepository.findById(usedProduct1.getUsedProductId()))
            .thenReturn(Optional.of(usedProduct1));
        when(inspectionRepository.findByUsedProduct(usedProduct1))
            .thenReturn(Optional.of(existingInspection));

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, 
            () -> inspectionService.addInspection(req));
        assertEquals(ErrorStatus._INSPECTION_ALREADY_EXISTS.getCode(), exception.getErrorReason().getCode());
        verify(inspectionRepository).findByUsedProduct(usedProduct1);
    }

    @DisplayName("검수 설명 수정")
    @Test
    void SUCCESS_updateInspectionDescription() {
        // given
        InspectionRequest.UpdateInspectionDescriptionRequest req = InspectionRequest.UpdateInspectionDescriptionRequest.builder()
                .inspectionId(inspection1.getInspectionId())
                .description("updated description")
                .build();

        when(inspectionRepository.findById(inspection1.getInspectionId()))
            .thenReturn(Optional.of(inspection1));
        when(inspectionRepository.save(any(Inspection.class)))
            .thenReturn(inspection1);

        // when
        InspectionDTO response = inspectionService.updateInspectionDescription(inspection1.getInspectionId(), req); 

        // then
        assertEquals(inspection1.getInspectionId(), response.inspectionId());
        assertEquals("updated description", response.description());
        verify(inspectionRepository).findById(inspection1.getInspectionId());
        verify(inspectionRepository).save(any(Inspection.class));
    }

}