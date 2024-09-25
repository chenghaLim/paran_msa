package com.paranmanzang.groupservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "point_detail")
public class PointDetail { //상세 포인트
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int point;

    @Builder.Default
    private LocalDate expirationAt = LocalDate.now().plusMonths(3);

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime transactionAt = LocalDateTime.now();

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "point_id")
    private Point parentPoint;

    public PointDetail(String status, int point, LocalDateTime transactionAt, Point beforepoint) {
        this.status = status;
        this.point = point;
        this.transactionAt = transactionAt;
        this.parentPoint = beforepoint;
    }

    public PointDetail(String status, int point, LocalDateTime transactionAt, LocalDate expirationAt, Point beforepoint) {
        this.status = status;
        this.point = point;
        this.transactionAt = transactionAt;
        this.expirationAt = expirationAt;
        this.parentPoint = beforepoint;
    }
}
