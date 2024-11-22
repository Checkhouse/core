package com.checkhouse.core.entity;

import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.dto.StockDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.geo.Point;

import java.util.UUID;

@Table(name = "stock")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql= "update stock st set st.deleted_at = now() where st.address_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Stock extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="stock_id")
    private UUID stockId;

    @Column(
            name="used_product_id",
            nullable = false
    )
    private UUID usedProductId;

    @Column(
            name="area",
            nullable = false
    )
    private String area;

    //foriegn key
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
    public Stock(
            UUID stockId,
            UUID usedProductId,
            String area,
            Hub hub
    ) {
        this.stockId = stockId;
        this.usedProductId = usedProductId;
        this.area = area;
        this.hub = hub;
    }

    public void updateArea(String area) {this.area = area;}
    public void updateHub(Hub hub) {this.hub = hub;}

    public StockDTO toDto() {
        return new StockDTO(
                this.stockId,
                this.usedProductId,
                this.area,
                this.hub.toDto()
        );
    }
}
