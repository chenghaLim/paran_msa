package com.paranmanzang.userservice.model.repository;/*
package com.paranmanzang.userservice.model.repository.user;

import com.paranmanzang.userservice.model.entity.user.LikePosts;
import com.paranmanzang.userservice.model.entity.user.LikeRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikePostRepository extends JpaRepository<LikePosts,Long> {
    // 사용자 ID와 게시판 ID로 LikePost 조회
    LikePosts findByUserIdAndPostId(Long userId, Long postId);

    // 사용자 ID와 게시판 ID로 LikePost 삭제
    boolean deleteByUserIdAndPostId(Long userId, Long postId);

    // 사용자 ID와 게시판 ID로 LikePost 조회
    Boolean existsByUserIdAndPostId(Long userId, Long postId);

    // 사용자 ID로 LikePost 목록 조회
    List<LikePosts> findByUserId(long userId);
}
*/
