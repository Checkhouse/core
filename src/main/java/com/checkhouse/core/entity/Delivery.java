package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.DeliveryState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "delivery")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="delivery_id")
    private UUID deliveryId;


    @Column(name="tracking_code")
    private String trackingCode;

    @Column(name = "state")
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
            Address address,
            String trackingCode,
            DeliveryState deliveryState
    ) {
        this.address = address;
        this.trackingCode = trackingCode;
        this.deliveryState = deliveryState;
    }
}
