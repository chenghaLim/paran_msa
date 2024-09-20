package com.paranmanzang.roomservice.service;


import com.paranmanzang.roomservice.model.domain.BookingModel;

import java.util.List;

public interface BookingService {
    Boolean save(BookingModel model);
    Boolean updateState(Long id);
    Boolean delete(Long id);

    List<?> findByGroup(long groupId);

    List<?> findByRoom(long roomId);

    BookingModel findOne(long id);

}
