package com.checkhouse.core.entity;

import com.checkhouse.core.dto.AddressDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "address")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update address addr set addr.deleted_at = now() where addr.address_id = :address_id")
@SQLRestriction("deleted_at IS NULL")
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
            UUID addressId,
            String name,
            String address,
            String addressDetail,
            Point location,
            int zipcode,
            String phone
    ) {
        this.addressId = addressId;
        this.name = name;
        this.address = address;
        this.addressDetail = addressDetail;
        this.location = location;
        this.zipcode = zipcode;
        this.phone = phone;
    }
    public void update(
            String name,
            String address,
            String addressDetail,
            int zipcode,
            Point location,
            String phone
    ) {
        this.name = name;
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
        this.location = location;
        this.phone = phone;
    }


    public AddressDTO toDto() {
        return new AddressDTO(
                this.addressId,
                this.name,
                this.address,
                this.zipcode,
                this.phone,
                this.addressDetail,
                this.location
        );
    }

}

