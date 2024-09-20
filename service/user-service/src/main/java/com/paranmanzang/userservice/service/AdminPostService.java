package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.domain.user.AdminPostModel;
import com.paranmanzang.userservice.model.entity.AdminPosts;

import java.util.List;
import java.util.Optional;

public interface AdminPostService {

    Boolean createAPost(AdminPostModel adminPostModel);

    // 게시글 수정
    boolean updateAPost(Long id, AdminPostModel adminPostModel);

    // 게시글 삭제
    boolean deleteAPost(Long id);

    // 게시글 리스트 조회
    List<AdminPosts> getAPost();

    // 게시글 상세 조회
    Optional<AdminPosts> getAPostById(Long id);

    // 조회수 확인
    Long getViewCount(Long id);
}
