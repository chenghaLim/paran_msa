package com.paranmanzang.roomservice.model.domain;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AddressModel {
    private Long id;
    @NotBlank(message = "공백은 입력 불가합니다.")
    private String address;
    @NotBlank(message = "공백은 입력 불가합니다.")
    private String detailAddress;
    @NotNull
    @Digits(integer = 2, fraction = 13, message = "위도의 범위를 벗어났습니다.")
    private Double latitude;
    @NotNull
    @Digits(integer = 3, fraction = 13, message = "경도의 범위를 벗어났습니다.")
    private Double longitude;
    @NotNull
    @Positive
    private Long roomId;
}
