package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "origin_image")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="origin_image_id")
    private UUID originImageId;


    //Foreign key
    @OneToOne
    @JoinColumn(name="image_id", nullable=false)
    private ImageURL image;

    @ManyToOne
    @JoinColumn(name="origin_product_id", nullable=false)
    private OriginProduct originProduct;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public OriginImage(
            ImageURL image,
            OriginProduct originProduct
    ) {
        this.image = image;
        this.originProduct = originProduct;
    }
}
