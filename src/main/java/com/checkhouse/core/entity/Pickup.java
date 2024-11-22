package com.checkhouse.core.entity;

import com.checkhouse.core.dto.PickupDTO;
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
public class Pickup extends BaseTimeEntity {
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
    public Pickup(
            UUID pickupId,
            Transaction transaction,
            Store store,
            Boolean isPicked_up
    ) {
        this.pickupId = pickupId;
        this.transaction = transaction;
        this.store = store;
        this.isPicked_up = isPicked_up;
    }

    public PickupDTO toDto() {
        return new PickupDTO(
                this.pickupId,
                this.transaction.toDto(),
                this.store.toDto(),
                this.isPicked_up
        );
    }

    public void updateState() {
        this.isPicked_up = true;
    }
}
