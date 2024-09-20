package com.paranmanzang.roomservice.model.repository.impl;

import com.paranmanzang.roomservice.model.entity.QRoom;
import com.paranmanzang.roomservice.model.entity.Room;
import com.paranmanzang.roomservice.model.repository.custom.RoomCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Room> findByNickname(String nickname) {
        QRoom room= QRoom.room;
        return jpaQueryFactory.selectFrom(room)
                .where(room.nickname.eq(nickname))
                .fetch();
    }
}
