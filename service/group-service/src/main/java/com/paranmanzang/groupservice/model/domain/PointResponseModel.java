package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Point;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointResponseModel {

    private Long id;
    private LocalDateTime createAt;
    private String detail;
    private int point;
    private Long groupId;
    private List<PointDetailResponseModel> pointDetails;

    public static PointResponseModel fromEntity(Point point) {
        return PointResponseModel.builder()
                .id(point.getId())
                .createAt(point.getCreateAt())
                .detail(point.getDetail())
                .point(point.getPoint())
                .groupId(point.getGroupId())
                .pointDetails(point.getPointDetails().stream()
                        .map(PointDetailResponseModel::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}

