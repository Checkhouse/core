package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "favorite_used")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteUsed extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="favorite_used_id")
    private UUID favoriteUsedId;


    //Foreign key
    @ManyToOne
    @JoinColumn(name="used_product_id", nullable = false)
    private UsedProduct usedProduct;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public FavoriteUsed(
            UsedProduct usedProduct,
            User user
    ) {
        this.usedProduct = usedProduct;
        this.user = user;
    }
}
