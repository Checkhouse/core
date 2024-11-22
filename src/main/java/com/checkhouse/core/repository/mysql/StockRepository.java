package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

}
