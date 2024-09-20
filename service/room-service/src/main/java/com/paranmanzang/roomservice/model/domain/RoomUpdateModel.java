package com.paranmanzang.roomservice.model.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateModel {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Positive
    private int maxPeople;
    @NotNull
    @Positive
    private int price;
    @NotNull
    private boolean opened;
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openTime;
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime;
}