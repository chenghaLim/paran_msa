package com.paranmanzang.commentservice.model.repository;


import com.paranmanzang.commentservice.model.domain.CommentResponseModel;

import java.util.List;

public interface CustomCommentRepository {

    List<CommentResponseModel> findCommentByPostId(Long postId);

    Boolean updateIncreaseStep(int ref, int step);

    int findRefByPostId(Long postId);

    Boolean updateCommentById(Long commentId);

    Boolean update(Long commentId, String content, String nickname);
}
