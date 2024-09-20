package com.paranmanzang.roomservice.service;


import com.paranmanzang.roomservice.model.domain.AccountCancelModel;
import com.paranmanzang.roomservice.model.domain.AccountResultModel;

public interface AccountService {
    Boolean requestPayment(AccountResultModel model);

    String findByOrderId(String orderId);

    Boolean cancel(AccountCancelModel model);

    Boolean cancel(Long bookingId);
}
