package com.checkhouse.core.entity;

import com.checkhouse.core.dto.ImageDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Table(name = "image_url")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE image_url SET deleted_at = NOW() WHERE image_id = ?")
@SQLRestriction("deleted_at is null")
public class ImageURL extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="image_id")
    private UUID imageId;

    @Column(
            name="image_url",
            nullable = false
    )
    private String imageURL;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public ImageURL(
            UUID imageId,
            String imageURL
    ) {
        this.imageId = imageId;
        this.imageURL = imageURL;

    }

    public ImageDTO toDto() {
        return new ImageDTO(imageId, imageURL);
    }
}
