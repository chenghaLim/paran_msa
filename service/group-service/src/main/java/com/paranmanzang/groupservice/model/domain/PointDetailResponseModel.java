package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.PointDetail;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointDetailResponseModel {

    private Long id;
    private String status;
    private int point;
    private LocalDate expirationAt;
    private LocalDateTime transactionAt;
    private Long parentPointId;  // parentPoint의 ID를 저장

    // PointDetail 엔티티를 받아서 PointDetailResponseModel로 변환하는 메서드
    public static PointDetailResponseModel fromEntity(PointDetail pointDetail) {
        return PointDetailResponseModel.builder()
                .id(pointDetail.getId())
                .status(pointDetail.getStatus())
                .point(pointDetail.getPoint())
                .expirationAt(pointDetail.getExpirationAt())
                .transactionAt(pointDetail.getTransactionAt())
                .parentPointId(pointDetail.getParentPoint() != null ? pointDetail.getParentPoint().getId() : null)
                .build();
    }
}
