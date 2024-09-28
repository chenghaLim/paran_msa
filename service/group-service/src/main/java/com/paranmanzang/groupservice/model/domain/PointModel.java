package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Point;
import com.paranmanzang.groupservice.model.entity.PointDetail;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointModel {

    private Long pointId;

    @NotNull(message = "그룹번호는 필수값입니다.")
    private Long groupId;
    @NotNull(message = "포인트 값은 필수값입니다.")
    private Integer point;
    private PointDetail pointDetail;
}
