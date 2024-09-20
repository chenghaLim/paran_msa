/*
package com.paranmanzang.userservice.service.impl;

import com.paranmanzang.userservice.model.domain.user.LikeRoomModel;
import com.paranmanzang.userservice.model.entity.LikeRooms;
import com.paranmanzang.userservice.model.entity.User;
import com.paranmanzang.userservice.model.repository.LikeRoomRepository;
import com.paranmanzang.userservice.model.repository.UserRepository;
import com.paranmanzang.userservice.service.LikeRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeRoomServiceImpl implements LikeRoomService {
    private final LikeRoomRepository likeRoomRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public LikeRoomServiceImpl(LikeRoomRepository likeRoomRepository, UserRepository userRepository, RoomRepository roomRepository) {
        this.likeRoomRepository = likeRoomRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    //좋아요
    public boolean add(LikeRoomModel likeRoomModel) {
        Long userId = likeRoomModel.getUserId();
        Long roomId = likeRoomModel.getRoomId();

        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Room> optionalRoom = roomRepository.findById(roomId);

        if(optionalUser.isPresent() && optionalRoom.isPresent()) {
            throw new RuntimeException("사용자나 방의 정보가 잘못되었습니다.");
        }

        if (likeRoomRepository.existsByUserIdAndRoomId(userId, roomId)) {
            System.out.println("test");
            return false; // 이미 좋아요를 눌렀으면 false 반환
        }

        LikeRooms likeRooms = new LikeRooms();
        likeRooms.setRoomId(roomId);
        likeRooms.setUser(optionalUser.get());
        likeRoomRepository.save(likeRooms);
        return true;
    }

    //좋아요 취소
    public boolean remove(LikeRoomModel likeRoomModel) {
        LikeRooms likeRooms = likeRoomRepository.findByUserIdAndRoomId(likeRoomModel.getUserId(), likeRoomModel.getRoomId());
        if (likeRooms != null) {
            likeRoomRepository.deleteByUserIdAndRoomId(likeRoomModel.getUserId(), likeRoomModel.getRoomId());
            return true;
        }
        return false;
    }

    //마이페이지 공간 찜 삭제
    public boolean removeLikeById(Long id) {
        likeRoomRepository.deleteById(id);
        System.out.println(!likeRoomRepository.existsById(id));
        return !likeRoomRepository.existsById(id);
    }

    //마이페이지 공간 찜 조회
    public List<LikeRoomModel> findAll(long userId) {
        List<LikeRooms> likeRooms = likeRoomRepository.findByUserId(userId);
        return likeRooms.stream()
                .map(likeRoom -> new LikeRoomModel(likeRoom.getId(),
                        likeRoom.getRoomId(),
                        likeRoom.getUser().getId()))
                .collect(Collectors.toList());
    }

    //공간 찜 확인 -> 수정
    public LikeRoomModel existsByUserIdAndRoomId(Long userId , Long roomId) {
        LikeRooms likeRooms = likeRoomRepository.findByUserIdAndRoomId(userId, roomId);
        if(likeRooms != null) {
            System.out.println(new LikeRoomModel(likeRooms.getId(), likeRooms.getRoomId(),likeRooms.getUser().getId()));
            return new LikeRoomModel(likeRooms.getId(), likeRooms.getRoomId(),likeRooms.getUser().getId());
        }
        System.out.println("null");
        return null;
    }

}


*/
