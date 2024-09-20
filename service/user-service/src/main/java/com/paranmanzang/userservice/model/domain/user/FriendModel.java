package com.paranmanzang.userservice.model.domain.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendModel {
    private Long id;
    private Long userId;
    private Long sendUserId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
