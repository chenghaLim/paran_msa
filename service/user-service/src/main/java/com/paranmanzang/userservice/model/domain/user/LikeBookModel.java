package com.paranmanzang.userservice.model.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeBookModel {
    private long id;
    private long userId;
    private long bookId;

    public LikeBookModel(long id, long bookId, long userId) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
    }
}
