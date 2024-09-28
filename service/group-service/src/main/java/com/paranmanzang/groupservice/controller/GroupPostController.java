package com.paranmanzang.groupservice.controller;

import com.paranmanzang.groupservice.model.domain.GroupPostModel;
import com.paranmanzang.groupservice.service.impl.GroupPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/grouppost")
@RequiredArgsConstructor
public class GroupPostController {
    private final GroupPostServiceImpl groupPostService;

    //#67 게시글 등록
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
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deletePost(@PathVariable Long boardId) {
        return ResponseEntity.ok(groupPostService.deletePost(boardId));
    }

    //#70 - 내가 속한 group의 게시물 목록
    @GetMapping("/{groupId}")
    public ResponseEntity<?> getAllByGroupId(@PathVariable Long groupId, @RequestParam int page, @RequestParam int size, @RequestParam String postCategory) {
        return ResponseEntity.ok(groupPostService.findByGroupId(groupId, PageRequest.of(page, size), postCategory));
    }

    // view 카운트 업데이트
    @PutMapping("/{postId}")
    public ResponseEntity<?> updateViewCount(@PathVariable Long postId) {
        return ResponseEntity.ok(groupPostService.updateViewCount(postId));
    }
}