package com.paranmanzang.userservice.model.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeRoomModel {
    private long id;
    private long roomId;
    private long userId;

    public LikeRoomModel(long id, long roomId, long userId) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
    }
}
