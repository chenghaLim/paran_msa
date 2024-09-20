package com.paranmanzang.roomservice.service.impl;


import com.paranmanzang.roomservice.model.domain.AccountCancelModel;
import com.paranmanzang.roomservice.model.domain.AccountResultModel;
import com.paranmanzang.roomservice.model.entity.Account;
import com.paranmanzang.roomservice.model.repository.AccountRepository;
import com.paranmanzang.roomservice.model.repository.impl.AccountRepositoryImpl;
import com.paranmanzang.roomservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountRepositoryImpl accountRepositoryImpl;


    @Override
    public Boolean requestPayment(AccountResultModel model) {

        return accountRepository.save(Account.builder()
                .orderId(model.getOrderId())
                .detail(model.getOrderName())
                .payToken(model.getPaymentKey())
                .usePoint(model.getUsePoint())
                .amount(model.getAmount())
                .roomId(model.getRoomId())
                .groupId(model.getGroupId())
                .bookingId(model.getBookingId())
                .build()) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public String findByOrderId(String orderId) {
        return accountRepositoryImpl.findPaymentKeyByOrderId(orderId).getPayToken();
    }

    @Override
    public Boolean cancel(AccountCancelModel model) {
        return accountRepositoryImpl.findAccountByPaymentKey(model.getPaymentKey()).map(account -> {
            account.setCanceled(true);
            account.setReason(model.getCancelReason());
            return accountRepository.save(account);
        }) == null ? Boolean.FALSE : Boolean.TRUE;

    }
    @Override
    public Boolean cancel(Long bookingId) {
        return accountRepositoryImpl.fondAccountByBookingId(bookingId).map(account -> {
            account.setCanceled(true);
            account.setReason("예약 취소");
            return accountRepository.save(account);
        }) == null ? Boolean.FALSE : Boolean.TRUE;

    }
}
