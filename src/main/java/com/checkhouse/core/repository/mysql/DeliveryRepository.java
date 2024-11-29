package com.checkhouse.core.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.enums.DeliveryState;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    // 배송 상태 수정
    @Modifying
    @Query("UPDATE Delivery d SET d.deliveryState = :state WHERE d.deliveryId = :deliveryId")
    void updateDeliveryState(@Param("deliveryId") UUID deliveryId, @Param("state") DeliveryState state);

    // 송장번호 업데이트
    @Modifying
    @Query("UPDATE Delivery d SET d.trackingCode = :trackingCode WHERE d.deliveryId = :deliveryId")
    void updateTrackingCode(@Param("deliveryId") UUID deliveryId, @Param("trackingCode") String trackingCode);

    // 배송 리스트 조회
    @Query("SELECT d FROM Delivery d WHERE d.address.user.userId = :userId")
    List<Delivery> findByUserId(@Param("userId") UUID userId);
}
