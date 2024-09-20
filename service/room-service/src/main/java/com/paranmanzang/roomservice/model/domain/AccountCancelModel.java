package com.paranmanzang.roomservice.model.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AccountCancelModel {
    private String paymentKey;
    private String cancelReason;
}
