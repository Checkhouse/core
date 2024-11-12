package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.TransactionState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
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


    @Column(name="state")
    private TransactionState state;


    //Foreign key
    @OneToOne
    @JoinColumn(name="used_product_id")
    private UsedProduct usedProduct;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User buyer;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Transaction(
            UsedProduct usedProduct,
            User buyer,
            TransactionState state
    ) {
        this.usedProduct = usedProduct;
        this.buyer = buyer;
        this.state = state;
    }
}
