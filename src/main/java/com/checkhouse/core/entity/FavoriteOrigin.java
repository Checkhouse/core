package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "favorite_origin")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteOrigin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="favorite_origin_id")
    private UUID favoriteOriginId;

    //Foreign key
    @ManyToOne
    @JoinColumn(name="origin_product_id")
    private OriginProduct originProduct;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;




    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public FavoriteOrigin(
            OriginProduct originProduct,
            User user
    ) {
        this.originProduct = originProduct;
        this.user = user;
    }
}
