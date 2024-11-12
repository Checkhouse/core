package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.PickupState;
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


    @Enumerated(EnumType.STRING)
    private PickupState state;


    //Foreign key
    @OneToOne
    @JoinColumn(name="transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name="store_id")
    private Store store;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public PickUp(
            Transaction transaction,
            Store store,
            PickupState state
    ) {
        this.transaction = transaction;
        this.store = store;
        this.state = state;
    }
}
