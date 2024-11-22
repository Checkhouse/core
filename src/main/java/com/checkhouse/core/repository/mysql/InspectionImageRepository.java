package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.InspectionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InspectionImageRepository extends JpaRepository<InspectionImage, UUID> {


}
