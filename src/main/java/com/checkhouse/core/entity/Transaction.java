package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "transaction")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="transaction_id")
    private UUID transactionId;


    @Column(
            name="is_completed",
            nullable = false
    )
    private Boolean isCompleted = false;


    //Foreign key
    @OneToOne
    @JoinColumn(
            name="used_product_id",
            nullable = false
    )
    private UsedProduct usedProduct;

    @ManyToOne
    @JoinColumn(
            name="user_id",
            nullable = false
    )
    private User buyer;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Transaction(
            UsedProduct usedProduct,
            User buyer,
            Boolean isCompleted
    ) {
        this.usedProduct = usedProduct;
        this.buyer = buyer;
        this.isCompleted = isCompleted;
    }
}
