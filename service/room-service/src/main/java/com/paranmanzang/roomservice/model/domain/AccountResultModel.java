package com.paranmanzang.roomservice.model.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AccountResultModel {
    private String orderId;
    private String paymentKey;
    private int amount;
    private String orderName;
    private Long groupId;
    private Long roomId;
    private Long bookingId;
    private int usePoint;
}
