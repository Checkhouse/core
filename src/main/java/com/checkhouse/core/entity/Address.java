package com.checkhouse.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

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

    @Column(
            name="name",
            nullable = false
    )
    private String name;

    @Column(
            name="address",
            nullable = false
    )
    private String address;

    @Column(name="address_detail")
    private String addressDetail;

    @Column(
            name="location",
            nullable = false
    )
    private Point location;

    @Column(
            name="zipcode",
            nullable = false
    )
    private int zipcode;

    @Column(
            name="phone",
            nullable = false
    )
    private String phone;

    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public Address(
            String name,
            String address,
            String addressDetail,
            Point location,
            int zipcode,
            String phone
    ) {
        this.name = name;
        this.address = address;
        this.addressDetail = addressDetail;
        this.location = location;
        this.zipcode = zipcode;
        this.phone = phone;
    }
}
