package com.paranmanzang.userservice.model.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPostModel {
    private Long id;
    private String title;
    private String content;
    private String category;
    private Long userId;
}
