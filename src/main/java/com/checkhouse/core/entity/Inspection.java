package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "inspection")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inspection extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="inspection_id")
    private UUID inspectionId;


    @Column(name="is_done")
    private boolean isDone;

    @Column(name="description")
    private String description;


    //Foreign key
    // TODO: 검수를 여러번 하는 경우가 존재하는가?
    @ManyToOne
    @JoinColumn(name="used_product_id")
    private UsedProduct usedProduct;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; //검수자

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Inspection(
            UsedProduct usedProduct,
            User user,
            boolean isDone,
            String description
    ) {
        this.usedProduct = usedProduct;
        this.user = user;
        this.isDone = isDone;
        this.description = description;
    }
}
