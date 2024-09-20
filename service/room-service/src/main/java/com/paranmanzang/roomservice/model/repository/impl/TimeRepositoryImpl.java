package com.paranmanzang.roomservice.model.repository.impl;

import com.paranmanzang.roomservice.model.domain.BookingModel;
import com.paranmanzang.roomservice.model.entity.QTime;
import com.paranmanzang.roomservice.model.entity.Time;
import com.paranmanzang.roomservice.model.repository.custom.TimeCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TimeRepositoryImpl implements TimeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Time> findByBooking(BookingModel model) {
        QTime time = QTime.time1;
       
        return jpaQueryFactory.selectFrom(time).where(
                time.date
                        .eq(LocalDate.of(model.getUsingStart().getYear(), model.getUsingStart().getMonth(),
                                model.getUsingStart().getDayOfMonth()))
                        .and(time.time.goe(LocalTime.of(model.getUsingStart().getHour(), 0)))
                        .and(time.time.loe(LocalTime.of(model.getUsingEnd().getHour(), 0))))
                .fetch();
    }

    @Override
    public void deleteByRoom(Long id) {
        QTime time = QTime.time1;
        jpaQueryFactory.delete(time).where(time.room.id.eq(id));
    }
}
