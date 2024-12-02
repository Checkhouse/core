package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.Negotiation;
import com.checkhouse.core.entity.enums.NegotiationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NegotiationRepository extends JpaRepository<Negotiation, UUID> {
    // 구매자 ID 조회
    @Query("select n from Negotiation n where n.buyer.userId = :buyerId")
    List<Negotiation> findAllByBuyerId(UUID buyerId);

    // 판매자 ID 조회
    @Query("select n from Negotiation n where n.seller.userId = :sellerId")
    List<Negotiation> findAllBySellerId(UUID sellerId);

    // 상태 조회    
    @Query("select n from Negotiation n where n.state = :state")
    List<Negotiation> findAllByState(NegotiationState state);



}
