package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.ImageURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.UUID;
@Repository
public interface ImageRepository extends JpaRepository<ImageURL, UUID> {


}
