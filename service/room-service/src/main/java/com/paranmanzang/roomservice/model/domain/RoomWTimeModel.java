package com.paranmanzang.roomservice.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class RoomWTimeModel {
    private Long id;
    private String name;
    private int maxPeople;
    private int price;
    private boolean opened;
    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalDateTime createdAt;
    private boolean enabled;
    private String nickname;
    private List<?> times;
}