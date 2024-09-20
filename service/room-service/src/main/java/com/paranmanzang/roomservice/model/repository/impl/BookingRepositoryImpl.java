package com.paranmanzang.roomservice.model.repository.impl;

import com.paranmanzang.roomservice.model.entity.Booking;
import com.paranmanzang.roomservice.model.entity.QBooking;
import com.paranmanzang.roomservice.model.repository.custom.BookingCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Booking> findByGroupId(Long id) {
        QBooking booking= QBooking.booking;
        return jpaQueryFactory.selectFrom(booking)
                .where(booking.groupId.eq(id)).fetch();
    }

    @Override
    public List<Booking> findByRoomId(Long id) {
        QBooking booking= QBooking.booking;
        return jpaQueryFactory.selectFrom(booking)
                .where(booking.room.id.eq(id)).fetch();
    }
}
