package com.paranmanzang.groupservice.model.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointRepositoryCustom {
    Page<?> findPointsByGroupId(Long groupId, Pageable pageable);
}
