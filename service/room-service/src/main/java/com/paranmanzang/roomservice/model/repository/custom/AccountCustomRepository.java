package com.paranmanzang.roomservice.model.repository.custom;


import com.paranmanzang.roomservice.model.entity.Account;

import java.util.Optional;

public interface AccountCustomRepository {
    Account findPaymentKeyByOrderId(String orderId);
    Optional<?> fondAccountByBookingId(Long bookingId);
    Optional<?> findAccountByPaymentKey(String payment);
}
