package com.checkhouse.core.entity;

import com.checkhouse.core.dto.UsedProductDTO;
import com.checkhouse.core.entity.enums.UsedProductState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Table(name = "used_product")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update used_product up set up.deleted_at = now() where up.used_product_id = :usedProductId")
@SQLRestriction("deleted_at is null")
public class UsedProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="used_product_id")
    private UUID usedProductId;


    @Column(
            name="title",
            nullable = false
    )
    private String title;

    @Column(
            name="description",
            nullable = false
    )
    private String description;

    @Column(
            name="price",
            nullable = false
    )
    private int price;

    @Column(
            name="is_nego_allow",
            nullable = false
    )
    private boolean isNegoAllow = true;

    @Column(
            name = "state",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private UsedProductState state = UsedProductState.PRE_SALE;


    //Foreign key
    @ManyToOne
    @JoinColumn(name="origin_product_id")
    private OriginProduct originProduct;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="address_id", nullable = false)
    private Address address;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public UsedProduct(
            UUID usedProductId,
            OriginProduct originProduct,
            User user,
            UsedProductState state,
            String title,
            String description,
            int price,
            boolean isNegoAllow,
            Address address
    ) {
        this.usedProductId = usedProductId;
        this.originProduct = originProduct;
        this.user = user;
        this.state = state;
        this.title = title;
        this.description = description;
        this.price = price;
        this.isNegoAllow = isNegoAllow;
        this.address = address;
    }
    public UsedProductDTO toDto() {
        return new UsedProductDTO(
                this.usedProductId,
                this.title,
                this.description,
                this.price,
                this.state,
                this.isNegoAllow,
                this.user.getUserId(),
                this.originProduct.getOriginProductId()
        );
    }

    public void updateUsedProductInfo(
            String title,
            String description,
            int price
    ) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public void updateUsedProductNegoAllow(boolean state) {
        this.isNegoAllow = state;
    }

    public void updateUsedProductOriginProduct() {}
    public void updateUsedProductState(UsedProductState status) {
        this.state = status;
    }
}
