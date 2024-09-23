package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Group;
import com.paranmanzang.groupservice.model.entity.GroupPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class GroupPostModel {
    private Long boardId;
    private String userBoardtitle;
    private String userBoardcontent;
    private Long userGroupId;

    public GroupPost toEntitysaving(){
        return GroupPost.builder()
                .content(this.userBoardtitle)
                .title(this.userBoardcontent)
                .group(new Group(this.userGroupId))
                .build();
    }
}
