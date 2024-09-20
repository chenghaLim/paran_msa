package com.paranmanzang.userservice.model.repository;

import com.paranmanzang.userservice.model.entity.DeclarationPosts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeclarationPostRepository extends JpaRepository<DeclarationPosts, Long> {
    List<DeclarationPosts> findByUserId(Long userId); // 유저 ID로 게시글 조회

}
