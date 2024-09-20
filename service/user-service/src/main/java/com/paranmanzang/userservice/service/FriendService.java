package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.domain.user.FriendModel;
import com.paranmanzang.userservice.model.entity.Friends;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FriendService {

    boolean createFriend( FriendModel friendModel);

    //친구 삭제
    boolean deleteFriend(Long id);

    //친구 리스트 조회
    List<Friends> listFriends();
}
