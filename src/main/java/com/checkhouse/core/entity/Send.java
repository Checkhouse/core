package com.checkhouse.core.entity;

import com.checkhouse.core.dto.SendDTO;
import com.checkhouse.core.entity.enums.DeliveryState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "send")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update send t set t.deleted_at = now() where t.send_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Send extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="send_id")
    private UUID sendId;

    @Column(
        name="state"
        )
    @Enumerated(EnumType.STRING)
    private DeliveryState state = DeliveryState.PRE_SEND;


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
    public void updateSendState(DeliveryState state) {this.state = state;}

    public SendDTO toDto(){
        return new SendDTO(sendId, transaction.toDto(), delivery.toDto(), state);
    }
}



