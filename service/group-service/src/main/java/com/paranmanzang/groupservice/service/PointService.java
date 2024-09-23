package com.paranmanzang.groupservice.service;

import com.paranmanzang.groupservice.model.domain.PointModel;

import java.time.LocalDate;

public interface PointService {
    Object addPoint(PointModel pointModel);

    Object deletePoint(Long pointId);

    void oldpoint(LocalDate today);

    Object usePoint(PointModel pointModel);

    Object searchPoint(Long groupId);
}
