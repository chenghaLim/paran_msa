package com.paranmanzang.groupservice.service;

import com.paranmanzang.groupservice.model.domain.PointModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PointService {
    Object addPoint(PointModel pointModel);

    Object deletePoint(Long pointId);

    void oldpoint(LocalDate today);

    Object usePoint(PointModel pointModel);

    Page<?> searchPoint(Long groupId, Pageable pageable);
}
