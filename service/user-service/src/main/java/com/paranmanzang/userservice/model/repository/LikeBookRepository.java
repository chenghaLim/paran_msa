package com.paranmanzang.userservice.model.repository;/*

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeBookRepository extends JpaRepository<User, Long> {
    // 사용자 ID와 책 ID로 LikeBook 조회
    likeBooks findByUserIdAndBookId(Long userId, Long bookId);

    // 사용자 ID와 책 ID로 LikeBook 삭제
    boolean deleteByUserIdAndBookId(Long userId, Long bookId);

    // 사용자 ID와 책 ID로 LikeBook 조회
    Boolean existsByUserIdAndBookId(Long userId, Long bookId);

    // 사용자 ID로 LikeBook 목록 조회
    List<likeBooks> findByUserId(long userId);
}
*/
