package com.checkhouse.core.entity;

import com.checkhouse.core.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "category")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="category_id")
    private UUID categoryId;

    @Column(
            name="name",
            nullable = false
    )
    private String name;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Category(
            UUID categoryId,
            String name
    ) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public CategoryDTO toDTO() {
        return new CategoryDTO(
                this.categoryId,
                this.name
        );
    }
}
