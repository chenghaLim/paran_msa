package com.paranmanzang.groupservice.model.repository.custom;

import com.paranmanzang.groupservice.model.domain.GroupPostResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupPostRepositoryCustom {
    Page<GroupPostResponseModel> findGroupPostByGroupId(Long groupId, Pageable pageable);
}
