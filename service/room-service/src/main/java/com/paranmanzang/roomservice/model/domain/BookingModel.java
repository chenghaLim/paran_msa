package com.paranmanzang.roomservice.model.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class BookingModel {
    private Long id;
    private boolean enabled;
    @NotNull(message = "이용시작 시간을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime usingStart;
    @NotNull(message = "이용 종료 시간을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime usingEnd;
    @NotNull
    @Positive
    private Long roomId;
    @NotNull
    @Positive
    private Long groupId;

}
