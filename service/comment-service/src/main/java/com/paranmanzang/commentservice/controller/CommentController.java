package com.paranmanzang.commentservice.controller;

import com.paranmanzang.commentservice.model.domain.CommentRequestModel;
import com.paranmanzang.commentservice.model.domain.CommentResponseModel;
import com.paranmanzang.commentservice.service.impl.CommentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping
    public ResponseEntity<Boolean> insert(@RequestBody @Valid CommentRequestModel model, @RequestHeader String nickname, BindingResult result)
            throws BindException {
        if (result.hasErrors()) throw new BindException(result);
        return ResponseEntity.ok(commentService.insert(model, nickname));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.delete(commentId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Boolean> update(@PathVariable Long commentId, @RequestBody String content
            , @RequestHeader String nickname)  {
        return ResponseEntity.ok(commentService.update(commentId,content, nickname));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseModel>> getCommentListByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentListByPostId(postId));
    }

}
