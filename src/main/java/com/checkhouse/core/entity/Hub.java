package com.checkhouse.core.entity;

import com.checkhouse.core.dto.HubDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Table(name = "hub")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update hub t set t.deleted_at = now() where t.hub_id = :hub_id")
@SQLRestriction("deleted_at IS NULL")
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
            UUID hubId,
            Address address,
            String name,
            int clusteredId
    ) {
        this.hubId = hubId;
        this.address = address;
        this.name = name;
        this.clusteredId = clusteredId;
    }

    public void UpdateAddress(Address address) {this.address = address;}
    public void UpdateName(String name) {this.name = name;}
    public void UpdateClusteredId(int clusteredId) {this.clusteredId = clusteredId;}

    public HubDTO toDTO() {
        return new HubDTO(hubId, name, clusteredId, address.toDTO());
    }
}
