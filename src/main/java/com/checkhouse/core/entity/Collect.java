package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "collect")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Collect extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="collect_id")
    private UUID collectId;


    //Foreign Key
    @OneToOne
    @JoinColumn(name="used_product_id", nullable=false)
    private UsedProduct usedProduct;

    @OneToOne
    @JoinColumn(name="delivery_id", nullable=false)
    private Delivery delivery;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Collect(
            UsedProduct usedProduct,
            Delivery delivery

    ) {
        this.usedProduct = usedProduct;
        this.delivery = delivery;
    }
}
