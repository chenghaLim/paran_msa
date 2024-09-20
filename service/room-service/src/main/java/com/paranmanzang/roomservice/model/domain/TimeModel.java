package com.paranmanzang.roomservice.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class TimeModel {
 private Long id;
 private LocalDate date;
 private LocalTime time;

}
