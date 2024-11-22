package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.checkhouse.core.dto.CollectDTO;
import com.checkhouse.core.entity.enums.DeliveryState;

@Table(name = "collect")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update collect t set t.deleted_at = now() where t.collect_id = :collect_id")
@SQLRestriction("deleted_at IS NULL")
public class Collect extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="collect_id")
    private UUID collectId;


    //Foreign Key
    @OneToOne
    @JoinColumn(
            name="used_product_id",
            nullable=false
    )
    private UsedProduct usedProduct;

    @OneToOne
    @JoinColumn(
            name="delivery_id",
            nullable=false
    )
    private Delivery delivery;

    @Column(
        name="state",
        nullable=false
        )
    @Enumerated(EnumType.STRING)
    private DeliveryState state = DeliveryState.COLLECTING;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Collect(
            UUID collectId,
            UsedProduct usedProduct,
            Delivery delivery,
            DeliveryState state
    ) {
        this.collectId = collectId;
        this.usedProduct = usedProduct;
        this.delivery = delivery;
        this.state = state;
    }

    public void updateCollectState(DeliveryState deliveryState) {this.state = deliveryState;}
    
    public CollectDTO toDto() {
        return new CollectDTO(collectId, usedProduct.toDto(), delivery.toDto(), state);
    }

}
