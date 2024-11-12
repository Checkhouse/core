package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "used_image")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsedImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="used_image_id")
    private UUID usedImageId;


    //Foreign key
    @OneToOne
    @JoinColumn(name="image_id")
    private ImageURL image;

    @ManyToOne
    @JoinColumn(name="used_product_id")
    private UsedProduct usedProduct;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public UsedImage(
            ImageURL image,
            UsedProduct usedProduct
    ) {
        this.image = image;
        this.usedProduct = usedProduct;
    }
}
