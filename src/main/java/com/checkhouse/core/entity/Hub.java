package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "hub")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="hub_id")
    private UUID hubId;


    @Column(
            name="name",
            nullable = false
    )
    private String name;

    @Column(
            name="clustered_id",
            nullable = false
    )
    private int clusteredId;


    //Foreign key
    @OneToOne
    @JoinColumn(
            name="address_id",
            nullable = false
    )
    private Address address;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Hub(
            Address address,
            String name,
            int clusteredId
    ) {
        this.address = address;
        this.name = name;
        this.clusteredId = clusteredId;
    }
}
