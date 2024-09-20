package com.paranmanzang.userservice.model.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeclarationPostModel {
    private Long id;
    private String title;
    private String content;
    private Long target;
    private Long userId;

}
