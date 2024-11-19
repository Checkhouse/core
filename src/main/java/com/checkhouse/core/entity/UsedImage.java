package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Table(name = "used_image")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update used_image t set t.deleted_at = now() where t.used_image_id = :used_image_id")
@SQLRestriction("deleted_at IS NULL")
public class UsedImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="used_image_id")
    private UUID usedImageId;


    //Foreign key
    @OneToOne
    @JoinColumn(
            name="image_id",
            nullable=false
    )
    private ImageURL image;

    @ManyToOne
    @JoinColumn(
            name="used_product_id",
            nullable=false
    )
    private UsedProduct usedProduct;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public UsedImage(
            UUID usedImageId,
            ImageURL image,
            UsedProduct usedProduct
    ) {
        this.usedImageId = usedImageId;
        this.image = image;
        this.usedProduct = usedProduct;
    }
}
