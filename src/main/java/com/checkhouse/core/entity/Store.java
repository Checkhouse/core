package com.checkhouse.core.entity;

import com.checkhouse.core.dto.StoreDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Table(name = "store")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update store t set t.deleted_at = now() where t.store_id = :store_id")
@SQLRestriction("deleted_at IS NULL")
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="store_id")
    private UUID storeId;


    @Column(
            name="name",
            nullable = false
    )
    private String name;

    @Column(
            name="code",
            nullable = false
    )
    private String code;


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
    public Store(
            UUID storeId,
            Address address,
            String name,
            String code
    ) {
        this.storeId = storeId;
        this.address = address;
        this.name = name;
        this.code = code;
    }

    public void updateName(String name) {this.name = name;}
    public void updateCode(String code) {this.code = code;}
    public void updateAddress(Address address) {this.address = address;}

    public StoreDTO toDto() {
        return new StoreDTO(
                this.storeId,
                this.name,
                this.code,
                this.address.toDto()
        );
    }
}
