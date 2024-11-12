package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.NegotiationState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Table(name = "negotiation")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Negotiation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="negotiation_id")
    private UUID negotiationId;

    @ManyToOne
    @JoinColumn(name="used_product_id")
    private UsedProduct usedProduct;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name="buyer_id")
    private User buyer;

    @Column(name="state")
    private NegotiationState state;

    @Column(name="price")
    private int price;

    //TODO: due_date mysql에서 관리할건지?
    @Column(name="due_date")
    private Date dueDate;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Negotiation(
            UsedProduct usedProduct,
            User seller,
            User buyer,
            NegotiationState state,
            int price,
            Date dueDate
    ) {
        this.usedProduct = usedProduct;
        this.seller = seller;
        this.buyer = buyer;
        this.state = state;
        this.price = price;
        this.dueDate = dueDate;
    }
}
