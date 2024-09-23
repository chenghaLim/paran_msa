package com.paranmanzang.groupservice.controller;

import com.paranmanzang.groupservice.model.domain.GroupPostModel;
import com.paranmanzang.groupservice.service.impl.GroupPostServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/grouppost")
public class GroupPostController {
    private GroupPostServiceImpl groupPostService;

    public GroupPostController(GroupPostServiceImpl groupPostService) {
        this.groupPostService = groupPostService;
    }

    //#67 - api request 수정 - 모임장만 등록 가능이 맞는지?
    //회원 전부 등록 가능하게 수정 시 userId, bookId, categoryId 넘겨야 함
    @PostMapping("/addboard")
    public ResponseEntity<?> add(@RequestBody GroupPostModel groupPostModel) {
        return ResponseEntity.ok(groupPostService.savePost(groupPostModel));
    }

    //#68. 게시글 수정
    @PutMapping("/updateboard")
    public ResponseEntity<?> update(@RequestBody GroupPostModel groupPostModel) {
        return ResponseEntity.ok(groupPostService.updatePost(groupPostModel));
    }

    //#69.게시글 삭제
    @DeleteMapping("/deleteboard")
    public ResponseEntity<?> deletePost(@RequestParam("boardId") Long boardId) {
        return ResponseEntity.ok(groupPostService.deletePost(boardId));
    }

    //#70 - 내가 속한 group의 게시물 목록
    @GetMapping("/boardlist")
    public ResponseEntity<?> getAllByGroupId(@RequestParam Long groupId) {
        return ResponseEntity.ok(groupPostService.findByGroupId(groupId));
    }

    //#게시글 상세 조회
    @GetMapping("/postDetail/{postId}")
    public ResponseEntity<?> getBoardPostByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(groupPostService.findById(postId));
    }

    //#미정의
    @GetMapping("/listByCategory/{categoryId}")
    public ResponseEntity<?> getBoardPostByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(groupPostService.findBycategoryId(categoryId));
    }
}