package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Group;
import com.paranmanzang.groupservice.model.entity.Joining;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResponseModel {

    private Long id;
    private String name;
    private String categoryName;
    private LocalDateTime createAt;
    private boolean enabled;
    private String detail;
    private String nickname;  // 관리자 nickname
    private String chatRoomId;

    // Group 엔티티를 받아서 GroupResponseModel로 변환하는 메서드
    public static GroupResponseModel fromEntity(Group group) {
        return GroupResponseModel.builder()
                .id(group.getId())
                .name(group.getName())
                .categoryName(group.getCategoryName())
                .createAt(group.getCreateAt())
                .enabled(group.isEnabled())
                .detail(group.getDetail())
                .nickname(group.getNickname())
                .chatRoomId(group.getChatRoomId())
                .build();
    }
}