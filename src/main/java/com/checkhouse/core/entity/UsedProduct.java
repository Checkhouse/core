package com.checkhouse.core.entity;

import com.checkhouse.core.entity.enums.UsedProductState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "used_product")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsedProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="used_product_id")
    private UUID usedProductId;

    @ManyToOne
    @JoinColumn(name="origin_product_id")
    private OriginProduct originProduct;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="state")
    private UsedProductState state;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private int price;

    @Column(name="is_nego_allow")
    private boolean isNegoAllow;



    //TODO: 이거면 되나? 더필요한내용은?

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public UsedProduct(
            OriginProduct originProduct,
            User user,
            UsedProductState state,
            String title,
            String description,
            int price,
            boolean isNegoAllow
    ) {
        this.originProduct = originProduct;
        this.user = user;
        this.state = state;
        this.title = title;
        this.description = description;
        this.price = price;
        this.isNegoAllow = isNegoAllow;
    }
}
