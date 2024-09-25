package com.paranmanzang.groupservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {
    Page<?> groupList(Pageable pageable);

    //참여중인 소모임 - error처리
    Page<?> groupsByUserNickname(String userNickname, Pageable pageable);

    Object enableGroup(Long groupId);

    Object enableCancelGroup(Long groupId);

    Boolean duplicatename(String groupname);

    Object deleteGroup(Long groupId);

    Object getGroupById(Long groupId);

    Object updateChatRoomId(String roomId, Long groupId);

    Page<?> enableGroupList(Pageable pageable);
}
