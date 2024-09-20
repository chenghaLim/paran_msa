package com.paranmanzang.roomservice.model.repository.custom;


import com.paranmanzang.roomservice.model.domain.BookingModel;

import java.util.List;

public interface TimeCustomRepository {
    List<?> findByBooking( BookingModel model);
    void deleteByRoom( Long id);
}
