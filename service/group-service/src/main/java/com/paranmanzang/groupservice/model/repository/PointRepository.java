package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findAllByGroupId(Long groupId);

    Optional findByGroupId(Long groupId);
}
