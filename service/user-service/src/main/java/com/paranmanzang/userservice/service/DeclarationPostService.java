package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.entity.DeclarationPosts;
import com.paranmanzang.userservice.model.domain.user.DeclarationPostModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeclarationPostService {
    // 신고 게시글 작성
    Boolean createDPost(DeclarationPostModel declarationPostModel);

    //신고 게시글 조회 (관리자 이거나 신고자 본인만)
    List<DeclarationPosts> getDPost(Long userId);

    //신고 게시글 상세 조회 (관리자 이거나 신고자 본인만)
    DeclarationPosts getPostDetail(Long postId, Long userId);

    //신고 게시글 접수 상태 변경(boolean값을 받아서 수락이면 카운트 메서드 진행 후 삭제 거절이면 그냥 삭제)
    boolean deleteDPost(boolean check, Long target, Long id);

}
