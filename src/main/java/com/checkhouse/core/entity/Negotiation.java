package com.checkhouse.core.entity;

import com.checkhouse.core.dto.NegotiationDTO;
import com.checkhouse.core.entity.enums.NegotiationState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    @Column(
            name="price",
            nullable = false
    )
    private int price;

    @Column(
            name = "state",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private NegotiationState state = NegotiationState.WAITING;


    //Foreign key
    @ManyToOne
    @JoinColumn(
            name="used_product_id",
            nullable = false
    )
    private UsedProduct usedProduct;

    @ManyToOne
    @JoinColumn(
            name="seller_id",
            nullable = false
    )
    private User seller;

    @ManyToOne
    @JoinColumn(
            name="buyer_id",
            nullable = false
    )
    private User buyer;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Negotiation(
            UUID negotiationId,
            UsedProduct usedProduct,
            User seller,
            User buyer,
            NegotiationState state,
            int price
    ) {
        this.negotiationId = negotiationId;
        this.usedProduct = usedProduct;
        this.seller = seller;
        this.buyer = buyer;
        this.state = state;
        this.price = price;
    }

    //toDTO
    public NegotiationDTO toDTO() {
        return new NegotiationDTO(
                this.negotiationId,
                this.usedProduct.toDto(),
                this.seller.toDto(),
                this.buyer.toDto(),
                this.state,
                this.price
                

        );
    }

    //update
    public void updateState(NegotiationState state) {
        this.state = state;
    }
    //get
    
}
