package com.paranmanzang.roomservice.model.repository.impl;

import com.paranmanzang.roomservice.model.entity.Account;
import com.paranmanzang.roomservice.model.entity.QAccount;
import com.paranmanzang.roomservice.model.repository.custom.AccountCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Account findPaymentKeyByOrderId(String orderId) {
        QAccount account= QAccount.account;
        return jpaQueryFactory.selectFrom(account).where(account.orderId.eq(orderId)).fetchFirst();
    }

    @Override
    public Optional<Account> fondAccountByBookingId(Long bookingId) {
        QAccount account= QAccount.account;
        return Optional.ofNullable(jpaQueryFactory.selectFrom(account)
                .where(account.bookingId.eq(bookingId)).fetchFirst());
    }

    @Override
    public Optional<Account> findAccountByPaymentKey(String payment) {
        QAccount account= QAccount.account;
        return Optional.ofNullable(jpaQueryFactory.selectFrom(account).where(account.payToken.eq(payment)).fetchFirst());
    }
}
