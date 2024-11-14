package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "origin_product")
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
            Category category,
            String name,
            String company
    ) {
        this.category = category;
        this.name = name;
        this.company = company;
    }
}
