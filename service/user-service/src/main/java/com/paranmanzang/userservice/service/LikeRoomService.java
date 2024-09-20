package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.domain.user.LikeRoomModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LikeRoomService {
    //좋아요
    boolean add(LikeRoomModel likeRoomModel);

    //좋아요 취소
    boolean remove(LikeRoomModel likeRoomModel);

    //마이페이지 공간 찜 삭제
    boolean removeLikeById(Long id);

    //마이페이지 공간 찜 조회
    List<LikeRoomModel> findAll(long userId);

    //공간 찜 확인
    LikeRoomModel existsByUserIdAndRoomId(Long userId , Long roomId);
}
