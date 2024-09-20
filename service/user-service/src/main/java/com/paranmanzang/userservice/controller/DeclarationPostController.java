package com.paranmanzang.userservice.controller;

import com.paranmanzang.userservice.model.domain.user.DeclarationPostModel;
import com.paranmanzang.userservice.model.entity.DeclarationPosts;
import com.paranmanzang.userservice.service.impl.DeclarationPostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depost")
public class DeclarationPostController {

    @Autowired
    private DeclarationPostServiceImpl declarationPostService;

    public DeclarationPostController(DeclarationPostServiceImpl declarationPostService) {
        this.declarationPostService = declarationPostService;
    }

    //신고 게시글 작성
    @PostMapping("/add")
    public ResponseEntity<?> createDPost(@RequestBody DeclarationPostModel declarationPostModel) {
        return ResponseEntity.ok( declarationPostService.createDPost(declarationPostModel));
    }
    //신고 게시글 조회 (관리자 이거나 신고자 본인만)
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<DeclarationPosts>> listDPost(@PathVariable Long userId) {
        return ResponseEntity.ok(declarationPostService.getDPost(userId));
    }

    //신고 게시글 상세 조회 (관리자 이거나 신고자 본인만)
    @GetMapping("/list/{postId}/{userId}")
    public ResponseEntity<DeclarationPosts> getAPostById(@PathVariable Long postId, @PathVariable Long userId) {
        try {
            DeclarationPosts post = declarationPostService.getPostDetail(postId, userId);
            return ResponseEntity.ok(post); // 성공 시 200 OK 응답과 함께 게시글 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 게시글이 없을 경우 404 반환
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 권한이 없을 경우 403 반환
        }
    }
    //신고 게시글 접수 상태 변경(boolean값을 받아서 수락이면 카운트 메서드 진행 후 삭제 거절이면 그냥 삭제)
    @DeleteMapping("/{postId}/complete")
    public ResponseEntity<?> completeDPost(@RequestParam boolean check, @RequestParam Long target, @PathVariable Long postId) {
        try {
            boolean result = declarationPostService.deleteDPost(check, target, postId);
            if (result) {
                return ResponseEntity.ok("신고 게시글이 성공적으로 처리되었습니다."); // 성공 메시지 반환
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류가 발생했습니다.");
        }
    }
}
