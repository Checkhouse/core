package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Table(name = "inspection_image")
@Entity
@Getter
@SQLDelete(sql = "UPDATE inspection_image SET deleted_at = NOW() WHERE inspection_image_id = ?")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InspectionImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="inspection_image_id")
    private UUID inspectionImageId;


    //Foreign key
    @OneToOne
    @JoinColumn(
            name="image_id",
            nullable=false
    )
    private ImageURL image;

    @OneToOne
    @JoinColumn(
            name="used_image_id",
            nullable = false
    )
    private UsedImage usedImage;

    @ManyToOne
    @JoinColumn(
            name="inspection_id",
            nullable = false
    )
    private Inspection inspection;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public InspectionImage(
            ImageURL image,
            Inspection inspection,
            UsedImage usedImage
    ) {
        this.image = image;
        this.inspection = inspection;
        this.usedImage = usedImage;
    }
}
