package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Point;
import com.paranmanzang.groupservice.model.entity.PointDetail;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PointModel {

    private Long pointId;

    @NotNull(message = "그룹번호는 필수값입니다.")
    private Long groupId;
    @NotNull(message = "포인트 값은 필수값입니다.")
    private Integer point;
    private PointDetail pointDetail;

    public PointDetail toEntity() {
        return PointDetail.builder()
                .status("적립")
                .point(this.point)
                .parentPoint(Point.builder()
                        .groupId(this.groupId)
                        .point(this.point)
                        .build())
                .build();
    }

    public PointDetail toEntityToAdd(int addpoint) {
        return PointDetail.builder()
                .status("적립")
                .point(this.point + addpoint)
                .parentPoint(Point.builder()
                        .groupId(this.groupId)
                        .point(this.point)
                        .build())
                .build();
    }
}
