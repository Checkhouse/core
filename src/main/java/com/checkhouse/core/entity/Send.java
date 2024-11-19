package com.checkhouse.core.entity;

import com.checkhouse.core.dto.SendDTO;
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

    @Column(
        name="state"
        )
    @Enumerated(EnumType.STRING)
    private DeliveryState state = DeliveryState.SENDING;


    //Foreign key
    @OneToOne
    @JoinColumn(
        name="transaction_id", 
        nullable=false
        )
    private Transaction transaction;

    @OneToOne
    @JoinColumn(
        name="delivery_id",
        nullable = false
        )
    private Delivery delivery;



    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Send(
            UUID sendId,
            Transaction transaction,
            Delivery delivery,
            DeliveryState state

    ) {
        this.sendId = sendId;
        this.transaction = transaction;
        this.delivery = delivery;
        this.state = state;
    }
    public void updateSendState(DeliveryState deliveryState) {
        this.state = deliveryState;
    }
    public SendDTO toDTO() {
        // return new SendDTO(sendId, transaction.toDTO(), delivery.toDTO(), state);
        return null;
    }
}

