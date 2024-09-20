package com.paranmanzang.userservice.controller;/*
package com.paranmanzang.userservice.controller.user;

import com.paranmanzang.userservice.model.domain.user.LikePostModel;
import com.paranmanzang.userservice.model.domain.user.LikeRoomModel;
import com.paranmanzang.userservice.service.user.impl.LikePostServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/likeposts")
public class LikePostController {

    private LikePostServiceImpl likePostService;

    public LikePostController(LikePostServiceImpl likePostService) {
        this.likePostService = likePostService;
    }

    //좋아요
    @PostMapping("/add")
    public ResponseEntity<?> likePost(@RequestBody LikePostModel likepostModel) {
        likePostService.add(likepostModel);
        return ResponseEntity.ok("like");
    }
    //좋아요 취소
    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@RequestBody LikePostModel likepostModel) {
        likePostService.remove(likepostModel);
        return ResponseEntity.ok("remove");
    }

    //좋아요 토글 확인
    @GetMapping("/{userId}/{postId}")
    public ResponseEntity<?> getLikeRoom(@PathVariable Long userId, @PathVariable Long postId) {
        return ResponseEntity.ok(likePostService.existsByUserIdAndPostId(userId, postId));
    }
    //좋아요 마이페이지 확인
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getLikeRoomList(@PathVariable Long userId) {
        return ResponseEntity.ok(likePostService.findAll(userId));
    }

    //좋아요 마이페이지 취소
    @DeleteMapping("/{likeRoomId}")
    public ResponseEntity<?> removeLikeRoom(@PathVariable Long likeRoomId) {
        likePostService.removeLikeById(likeRoomId);
        return ResponseEntity.ok("remove");
    }
}
*/
