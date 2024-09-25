package com.paranmanzang.roomservice.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "예약 정보")
public class BookingModel {
    @Schema(title = "예약 id")
    private Long id;
    @Schema(title = "승인 여부", description = "공간 등록자의 승인입니다.")
    private boolean enabled;
    @Schema(title = "이용 시작 시간")
    @NotNull(message = "이용시작 시간을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime usingStart;
    @Schema(title = "이용 종료시간", description = "HH:59 까지 이용 가능합니다.")
    @NotNull(message = "이용 종료 시간을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime usingEnd;
    @Schema(title = "공간 id", description = "예약한 곳입니다.")
    @NotNull
    @Positive
    private Long roomId;
    @Schema(title = "소모임 id", description = "예약한 주체입니다.")
    @NotNull
    @Positive
    private Long groupId;

}
