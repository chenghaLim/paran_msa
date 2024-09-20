/*
package com.paranmanzang.userservice.controller;

import com.paranmanzang.userservice.model.domain.user.LikeRoomModel;
import com.paranmanzang.userservice.service.impl.LikeRoomServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likerooms")
public class LikeRoomController {
    private final LikeRoomServiceImpl likeRoomService;

    public LikeRoomController(LikeRoomServiceImpl likeRoomService) {
        this.likeRoomService = likeRoomService;
    }

    //좋아요
    @PostMapping("/add")
    public ResponseEntity<?> likeRoom(@RequestBody LikeRoomModel likeRoomModel) {
        likeRoomService.add(likeRoomModel);
        return ResponseEntity.ok("like");
    }
    //좋아요 취소
    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@RequestBody LikeRoomModel likeRoomModel) {
        likeRoomService.remove(likeRoomModel);
        return ResponseEntity.ok("remove");
    }

    //좋아요 토글 확인
    @GetMapping("/{userId}/{roomId}")
    public ResponseEntity<?> getLikeRoom(@PathVariable Long userId, @PathVariable Long roomId) {
        return ResponseEntity.ok(likeRoomService.existsByUserIdAndRoomId(userId, roomId));
    }
    //좋아요 마이페이지 확인
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getLikeRoomList(@PathVariable Long userId) {
        return ResponseEntity.ok(likeRoomService.findAll(userId));
    }

    //좋아요 마이페이지 취소
    @DeleteMapping("/{likeRoomId}")
    public ResponseEntity<?> removeLikeRoom(@PathVariable Long likeRoomId) {
        likeRoomService.removeLikeById(likeRoomId);
        return ResponseEntity.ok("remove mypage likeroom");
    }
}
*/
