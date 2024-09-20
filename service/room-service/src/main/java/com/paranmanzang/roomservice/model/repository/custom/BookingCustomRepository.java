package com.paranmanzang.roomservice.model.repository.custom;


import com.paranmanzang.roomservice.model.entity.Booking;

import java.util.List;

public interface BookingCustomRepository {
    List<Booking> findByGroupId(Long id);
    List<Booking> findByRoomId( Long id);
}
