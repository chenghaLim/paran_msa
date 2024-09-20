package com.paranmanzang.roomservice.model.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ReviewModel {
    private Long id;
    @NotNull
    @Positive(message = "자연수만 입력 가능합니다.")
    @Max(value = 10, message = "최대 입력값은 10 입니다.")
    private int rating;
    @NotBlank(message = "공백은 입력 불가합니다.")
    private String content;
    @NotBlank
    private String nickname;
    private LocalDateTime createAt;
    @NotNull
    @Positive
    private Long roomId;
    @NotNull
    @Positive
    private Long bookingId;
}
