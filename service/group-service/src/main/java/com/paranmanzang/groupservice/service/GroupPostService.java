package com.paranmanzang.groupservice.service;

import com.paranmanzang.groupservice.model.domain.GroupPostModel;
import com.paranmanzang.groupservice.model.domain.GroupPostResponseModel;

import java.util.List;

public interface GroupPostService {
    List<GroupPostResponseModel> findAll();

    GroupPostResponseModel findById(Long id);

    List<GroupPostResponseModel> findBycategoryId(Long categoryId);

    List<GroupPostResponseModel> findByGroupId(Long groupId);

    Boolean savePost(GroupPostModel groupPostModel);
}
