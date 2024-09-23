package com.paranmanzang.groupservice.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "points")
public class Point { //포인트 합계
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();

    @Builder.Default
    private String detail = "적립";

    @Builder.Default
    private int point = 0;

    @Column(nullable = false)
    private Long groupId;

    @OneToMany(mappedBy = "point", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonManagedReference
    private List<PointDetail> pointDetails = new ArrayList<>();
}
