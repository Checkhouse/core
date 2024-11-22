package com.checkhouse.core.entity;

import com.checkhouse.core.dto.UserAddressDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "user_address")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddress extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="user_address_id")
    private UUID userAddressId;


    //Foreign key
    @OneToOne
    @JoinColumn(
            name="address_id",
            nullable=false
    )
    private Address address;

    @ManyToOne
    @JoinColumn(
            name="user_id",
            nullable=false
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name="hub_id",
            nullable = false
    )
    private Hub hub;




    //----------------------------------------------------------------------------
    // todo 관계 매핑 하는 곳
    //----------------------------------------------------------------------------

    @Builder
    public UserAddress(
            UUID userAddressId,
            Address address,
            User user,
            Hub hub
    ) {
        this.userAddressId = userAddressId;
        this.address = address;
        this.user = user;
        this.hub = hub;
    }
    public void UpdateHub(Hub newhub) {
        this.hub = newhub;
    }

    public UserAddressDTO toDto() {
        return new UserAddressDTO(
                this.userAddressId,
                this.address.toDto(),
                this.user.toDto(),
                this.hub.toDto()
        );
    }

}
