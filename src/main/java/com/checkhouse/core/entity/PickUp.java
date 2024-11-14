package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "pickup")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickUp extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="pickup_id")
    private UUID pickupId;


    @Column(
            name="is_picked_up",
            nullable = false
    )
    private Boolean isPicked_up = false;


    //Foreign key
    @OneToOne
    @JoinColumn(
            name="transaction_id",
            nullable = false
    )
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(
            name="store_id",
            nullable = false
    )
    private Store store;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public PickUp(
            Transaction transaction,
            Store store,
            Boolean isPicked_up
    ) {
        this.transaction = transaction;
        this.store = store;
        this.isPicked_up = isPicked_up;
    }
}
