package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.DeliveryState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "send_delivery")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendDelivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="send_delivery_id")
    private UUID sendDeliveryId;

    @OneToOne
    @JoinColumn(name="transaction_id")
    private Transaction transaction;

    @OneToOne
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    @Column(name="state")
    private DeliveryState state;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public SendDelivery(
            Transaction transaction,
            Delivery delivery,
            DeliveryState state

    ) {
        this.transaction = transaction;
        this.delivery = delivery;
        this.state = state;
    }
}
