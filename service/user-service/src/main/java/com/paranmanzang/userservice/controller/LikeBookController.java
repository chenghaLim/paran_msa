package com.paranmanzang.userservice.controller;/*
package com.paranmanzang.userservice.controller.user;

import com.paranmanzang.userservice.model.domain.user.LikeBookModel;
import com.paranmanzang.userservice.service.user.impl.LikeBookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/likebooks")
public class LikeBookController {

    private LikeBookServiceImpl likeBookService;

    public LikeBookController(LikeBookServiceImpl likeBookService) {
        this.likeBookService = likeBookService;
    }

    //좋아요
    @PostMapping("/add")
    public ResponseEntity<?> likeRoom(@RequestBody LikeBookModel likeBookModel) {
        likeBookService.add(likeBookModel);
        return ResponseEntity.ok("like");
    }
    //좋아요 취소
    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@RequestBody LikeBookModel likeBookModel) {
        likeBookService.remove(likeBookModel);
        return ResponseEntity.ok("remove");
    }

    //좋아요 토글 확인
    @GetMapping("/{userId}/{bookId}")
    public ResponseEntity<?> getLikeRoom(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(likeBookService.existsByUserIdAndBookId(userId, bookId));
    }
    //좋아요 마이페이지 확인
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getLikeRoomList(@PathVariable Long userId) {
        return ResponseEntity.ok(likeBookService.findAll(userId));
    }

    //좋아요 마이페이지 취소
    @DeleteMapping("/{likeRoomId}")
    public ResponseEntity<?> removeLikeRoom(@PathVariable Long likeRoomId) {
        likeBookService.removeLikeById(likeRoomId);
        return ResponseEntity.ok("remove mypage likeroom");
    }
}
*/
