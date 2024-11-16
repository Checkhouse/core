package com.checkhouse.core.entity;

import com.checkhouse.core.dto.OriginProductDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(
        name = "origin_product",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name"})
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="origin_product_id")
    private UUID originProductId;


    @Column(
            name="name",
            nullable = false,
            unique = true
    )
    private String name;

    @Column(
            name="company",
            nullable = false
    )
    private String company;


    //Foreign Key
    @ManyToOne
    @JoinColumn(
            name="category_id",
            nullable = false
    )
    private Category category;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public OriginProduct(
            UUID id,
            Category category,
            String name,
            String company
    ) {
        this.originProductId = id;
        this.category = category;
        this.name = name;
        this.company = company;
    }

    public OriginProductDTO toDto(){
        return new OriginProductDTO(
                this.originProductId,
                this.name,
                this.company,
                this.category
        );
    }
    public void updateOriginProductInfo(String name, String company) {
        this.name = name;
        this.company = company;
    }

    public void updateOriginProductCategory(Category category) {
        this.category = category;
    }
}
