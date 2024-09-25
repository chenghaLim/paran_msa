package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.entity.Point;
import com.paranmanzang.groupservice.model.repository.custom.PointRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {
    Optional findByGroupId(Long groupId);
}
