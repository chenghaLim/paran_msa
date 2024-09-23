package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.entity.PointDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PointDetailRepository extends JpaRepository<PointDetail, Long> {
    List<PointDetail> findAllByExpirationAt(LocalDate expiration);
}
