package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {


}
