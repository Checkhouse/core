package com.checkhouse.core.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {


}
