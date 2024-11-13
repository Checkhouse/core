package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "store")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="store_id")
    private UUID storeId;


    @Column(name="name", nullable = false)
    private String name;

    @Column(name="code", nullable = false)
    private String code;


    //Foreign key
    @OneToOne
    @JoinColumn(name="address_id", nullable = false)
    private Address address;


    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Store(
            Address address,
            String name,
            String code
    ) {
        this.address = address;
        this.name = name;
        this.code = code;
    }
}
