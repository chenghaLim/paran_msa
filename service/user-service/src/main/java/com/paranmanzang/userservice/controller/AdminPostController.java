package com.paranmanzang.userservice.controller;

import com.paranmanzang.userservice.model.domain.user.AdminPostModel;
import com.paranmanzang.userservice.model.entity.AdminPosts;
import com.paranmanzang.userservice.service.impl.AdminPostServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/aboard")
public class AdminPostController {

    private final AdminPostServiceImpl adminPostService;

    public AdminPostController(AdminPostServiceImpl adminPostService) {
        this.adminPostService = adminPostService;
    }

    // 게시글 작성
    @PostMapping("/add")
    public ResponseEntity<?> createAPost(@RequestBody AdminPostModel adminPostModel) {
        adminPostService.createAPost(adminPostModel);
        return ResponseEntity.ok("create");
    }
    // 게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAPost(@PathVariable Long id, @RequestBody AdminPostModel adminPostModel) {
        adminPostService.updateAPost(id, adminPostModel);
        return ResponseEntity.ok("update");
    }
    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAPost(@PathVariable Long id) {
        adminPostService.deleteAPost(id);
        return ResponseEntity.ok("delete");
    }
    // 게시글 조회
    @GetMapping("/list")
    public ResponseEntity<List<AdminPosts>> getAllAPosts() {
        return ResponseEntity.ok(adminPostService.getAPost());
    }

    // 게시글 상세 조회
    @GetMapping("/list/{id}")
    public ResponseEntity<AdminPosts> getAPostById(@PathVariable Long id) {
        return adminPostService.getAPostById(id)
                .map(post -> ResponseEntity.ok(post))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 게시글 조회수 확인
    @GetMapping("/viewCount/{id}")
    public ResponseEntity<Long> viewCount(@PathVariable Long id) {
        return ResponseEntity.ok(adminPostService.getViewCount(id));
    }

}
