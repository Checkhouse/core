package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.checkhouse.core.dto.InspectionDTO;

import com.checkhouse.core.dto.InspectionDTO;

@Table(name = "inspection")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update inspection t set t.deleted_at = now() where t.inspection_id = :inspection_id")
@SQLRestriction("deleted_at IS NULL")
public class Inspection extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="inspection_id")
    private UUID inspectionId;


    @Column(
            name="is_done",
            nullable = false
    )
    private boolean isDone = false;

    @Column(name="description")
    private String description;


    //Foreign key
    @OneToOne
    @JoinColumn(
            name="used_product_id",
            nullable = false
    )
    private UsedProduct usedProduct;

    @ManyToOne
    @JoinColumn(
            name="user_id",
            nullable = false
    )
    private User user; //검수자

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Inspection(
            UUID inspectionId,
            UsedProduct usedProduct,
            User user,
            boolean isDone,
            String description
    ) {
        this.inspectionId = inspectionId;
        this.usedProduct = usedProduct;
        this.user = user;
        this.isDone = isDone;
        this.description = description;
    }

    public void updateInspectionState(boolean isDone) {
        this.isDone = isDone;
    }
    
    public InspectionDTO toDTO() {
        return new InspectionDTO(inspectionId, isDone, description, usedProduct.toDto(), user.toDto());
    }

}
