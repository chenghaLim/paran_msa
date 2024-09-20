package com.paranmanzang.userservice.service.impl;

import com.paranmanzang.userservice.model.domain.user.FriendModel;
import com.paranmanzang.userservice.model.entity.Friends;
import com.paranmanzang.userservice.model.repository.FriendRepository;
import com.paranmanzang.userservice.model.repository.UserRepository;
import com.paranmanzang.userservice.service.FriendService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
//friendId : id, 친구요청 받는 친구 user_id, 친구 요청 보내는 친구  send_user_id, request_at, response_at



@Service
public class FriendServiceImpl implements FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public FriendServiceImpl(UserRepository userRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    //친구 추가
    public boolean createFriend(FriendModel friendModel) {
        boolean isFriendRequestExist = friendRepository.existsBySendUserIdAndUserId(friendModel.getSendUserId(), friendModel.getUserId());

        if (isFriendRequestExist) {
            System.out.println("실패");
            return false;
        }
        Friends friends = friendRepository.save(Friends.builder()
                .user(userRepository.findById(friendModel.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다.")))
                .sendUser(userRepository.findById(friendModel.getSendUserId())
                        .orElseThrow(() -> new IllegalArgumentException("보낸 유저가 없습니다.")))
                .response_at(LocalDateTime.now()) // response_at에 현재 시간 저장
                .build());
        System.out.println(friends);
        return friends != null;
    }

    //친구 삭제
    public boolean deleteFriend(Long id) {

        if (!friendRepository.existsById(id)) {
            return false;
        }
        friendRepository.deleteById(id);
        return true;
    }

    //친구 리스트 조회
    public List<Friends> listFriends() {
        return friendRepository.findAll();
    }
}
