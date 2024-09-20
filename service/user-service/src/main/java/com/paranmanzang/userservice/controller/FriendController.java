package com.paranmanzang.userservice.controller;

import com.paranmanzang.userservice.model.domain.user.FriendModel;
import com.paranmanzang.userservice.model.entity.Friends;
import com.paranmanzang.userservice.service.impl.FriendServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendServiceImpl friendService;
    public FriendController(FriendServiceImpl friendService) {
        this.friendService = friendService;
    }
    //친구 등록
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody FriendModel friendModel) {
        friendService.createFriend(friendModel);
        return ResponseEntity.ok("success");
    }
    //친구 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        friendService.deleteFriend(id);
        return ResponseEntity.ok("success");
    }
    //친구 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<Friends>> list() {
        return ResponseEntity.ok(friendService.listFriends());
    }
}
