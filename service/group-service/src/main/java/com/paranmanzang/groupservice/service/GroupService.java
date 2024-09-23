package com.paranmanzang.groupservice.service;

import com.paranmanzang.groupservice.model.entity.Group;

import java.util.List;

public interface GroupService {
    List<Group> groupList();

    Group oneGroup(Long groupId);

    List<Group> groupsByCategory(String categoryId);

    //참여중인 소모임 - error처리
    List<Object> groupsByUserNickname(String userNickname);

    Object enableGroup(Long groupId);

    Object enableCancelGroup(Long groupId);

    Object addLeader(Long groupId, String nickname);

    Boolean duplicatename(String groupname);

    Object deleteGroup(Long groupId);
}
