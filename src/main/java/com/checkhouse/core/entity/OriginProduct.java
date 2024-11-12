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

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @Column(name="name")
    private String name;

    @Column(name="company")
    private String company;

    //TODO: 이거면 되나? 더필요한내용은?

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
