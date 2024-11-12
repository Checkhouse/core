package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.DeliveryState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
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

    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;

    @Column(name="tracking_code", nullable=true)
    private String trackingCode;

    @Column(name="state")
    private DeliveryState deliveryState;

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
