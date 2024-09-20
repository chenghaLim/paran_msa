package com.paranmanzang.roomservice.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TimeSaveModel {
    private Long roomId;
    private int openTime;
    private int closeTime;
}
