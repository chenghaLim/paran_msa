package com.paranmanzang.userservice.model.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LikePostModel {
    private long id;
    private long postId;
    private long userId;

    public LikePostModel(long id, long postId, long userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }
}
