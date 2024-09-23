package com.paranmanzang.groupservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "joining")
public class Joining {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT")
    private boolean enabled;

    @Column(nullable = false)
    private String nickname;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private Group group;
}
