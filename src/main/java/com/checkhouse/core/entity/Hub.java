package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
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

    @OneToOne
    @JoinColumn(name="address_id")
    private Address address;

    @Column(name="name")
    private String name;

    @Column(name="clustered_id")
    private UUID clusteredId; //TODO: UUID 맞는지?

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Hub(
            Address address,
            String name,
            UUID clusteredId
    ) {
        this.address = address;
        this.name = name;
        this.clusteredId = clusteredId;
    }
}
