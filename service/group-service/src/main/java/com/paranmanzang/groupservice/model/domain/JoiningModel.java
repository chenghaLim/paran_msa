package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Group;
import com.paranmanzang.groupservice.model.entity.Joining;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class JoiningModel {
    @NotBlank(message = "참여할 사용자 닉네임은 필수값입니다.")
    private String nickname;
    @NotNull(message = "참여할 그룹 Id는 필수값입니다.")
    private Long groupId;

    private Group grouptojoin;

    public Joining toEntity(){
        return Joining.builder()
                .nickname(this.nickname)
                .group(this.grouptojoin)
                .build();
    }

}