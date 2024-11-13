package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.DeliveryState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "send")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Send extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="send_id")
    private UUID sendId;


    @Enumerated(EnumType.STRING)
    private DeliveryState state;


    //Foreign key
    @OneToOne
    @JoinColumn(name="transaction_id", nullable=false)
    private Transaction transaction;

    @OneToOne
    @JoinColumn(name="delivery_id", nullable = false)
    private Delivery delivery;



    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Send(
            Transaction transaction,
            Delivery delivery,
            DeliveryState state

    ) {
        this.transaction = transaction;
        this.delivery = delivery;
        this.state = state;
    }
}
