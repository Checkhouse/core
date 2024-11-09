package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "address")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="address_id")
    private UUID addressId;

    @Column(name="name")
    private String name;

    @Column(name="address")
    private String address;

    @Column(name="zipcode")
    private int zipcode;

    @Column(name="phone")
    private int phone;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Address(
            String name,
            String address,
            int zipcode,
            int phone
    ) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phone = phone;
    }
}
