package com.paranmanzang.groupservice.controller;

import com.paranmanzang.groupservice.model.domain.LikeBookModel;
import com.paranmanzang.groupservice.service.impl.LikeBookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/likebook")
@RequiredArgsConstructor
public class LikeBooksController {
    private final LikeBookServiceImpl likeBookService;

    //좋아요
    @PostMapping("/add")
    public ResponseEntity<?> likeBook(@RequestBody LikeBookModel likeBookModel) throws BindException {
        return ResponseEntity.ok(likeBookService.add(likeBookModel));
    }

    //좋아요 취소
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeLikeBook(@RequestBody LikeBookModel likeBookModel) throws BindException {
        return ResponseEntity.ok(likeBookService.remove(likeBookModel));
    }

    //좋아요 마이페이지 확인
    @GetMapping("/list/{nickname}")
    public ResponseEntity<?> getLikeRoomList(@PathVariable String nickname, BindingResult bindingResult)
            throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return ResponseEntity.ok(likeBookService.findAllByNickname(nickname));
    }
}
