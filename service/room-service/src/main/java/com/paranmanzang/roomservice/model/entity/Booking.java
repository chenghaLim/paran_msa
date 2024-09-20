package com.paranmanzang.roomservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT")
    private boolean enabled;
    @Column(nullable = false)
    private LocalDateTime usingStart;
    @Column(nullable = false)
    private LocalDateTime usingEnd;
    @Column(nullable = false)
    private Long groupId;
    @CreatedDate
    private LocalDateTime createAt;
    private LocalDateTime responseAt;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "room_id")
    private Room room;
    @OneToOne(mappedBy = "booking")
    private Review reviews;
}
