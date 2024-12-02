package com.checkhouse.core.entity;

import com.checkhouse.core.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Table(name = "category")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE category SET deleted_at = NOW() WHERE category_id = ?")
@SQLRestriction("deleted_at is null")
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

    public void updateName(String name) { this.name = name; }

    public CategoryDTO toDto() {
        return new CategoryDTO(
                this.categoryId,
                this.name
        );
    }
}
