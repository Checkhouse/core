package com.checkhouse.core.entity;

import com.checkhouse.core.dto.DeliveryDTO;
import com.checkhouse.core.entity.enums.DeliveryState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "delivery")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update delivery t set t.deleted_at = now() where t.delivery_id = :delivery_id")
@SQLRestriction("deleted_at IS NULL")
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name="delivery_id"
    )
    private UUID deliveryId;


    @Column(
            name="tracking_code"
    )
    private String trackingCode;

    @Column(
            name = "state",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState = DeliveryState.PRE_DELIVERY;


    //Foreign key
    @ManyToOne
    @JoinColumn(
            name="address_id",
            nullable=false
    )
    private Address address;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Delivery(
            UUID deliveryId,
            Address address,
            String trackingCode,
            DeliveryState deliveryState
    ) {
        this.deliveryId = deliveryId;
        this.address = address;
        this.trackingCode = trackingCode;
        this.deliveryState = deliveryState;
    }

    public void UpdateAddress(Address address) {this.address = address;}
    public void UpdateTrackingCode(String trackingCode) {this.trackingCode = trackingCode;}
    public void UpdateDeliveryState(DeliveryState deliveryState) {this.deliveryState = deliveryState;}

    public DeliveryDTO toDTO() {
        return new DeliveryDTO(deliveryId, trackingCode, deliveryState, address.toDTO());
    }
}
