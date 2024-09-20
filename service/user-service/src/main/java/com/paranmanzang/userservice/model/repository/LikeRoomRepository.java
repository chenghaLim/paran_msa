package com.paranmanzang.userservice.model.repository;

import com.paranmanzang.userservice.model.entity.LikeRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRoomRepository extends JpaRepository<LikeRooms,Long> {
    // 사용자 ID와 룸 ID로 LikeRooms 조회
    LikeRooms findByUserIdAndRoomId(Long userId, Long roomId);

    // 사용자 ID와 룸 ID로 LikeRoom 삭제
    boolean deleteByUserIdAndRoomId(Long userId, Long roomId);

    // 사용자 Id와 룸 ID로 Likeroom 조회
    Boolean existsByUserIdAndRoomId(Long userId, Long roomId);

    // 사용자 ID로 Likeroom 목록 조회
    List<LikeRooms> findByUserId(long userId);
}
