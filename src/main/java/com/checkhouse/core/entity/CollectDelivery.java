package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "collect_delivery")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectDelivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="collect_delivery_id")
    private UUID collectDeliveryId;


    //Foreign Key
    @OneToOne
    @JoinColumn(name="used_product_id")
    private UsedProduct usedProduct;

    @OneToOne
    @JoinColumn(name="delivery_id")
    private Delivery delivery;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public CollectDelivery(
            UsedProduct usedProduct,
            Delivery delivery

    ) {
        this.usedProduct = usedProduct;
        this.delivery = delivery;
    }
}
