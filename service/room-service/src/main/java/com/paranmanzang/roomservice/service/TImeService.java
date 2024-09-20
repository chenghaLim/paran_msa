package com.paranmanzang.roomservice.service;


import com.paranmanzang.roomservice.model.domain.BookingModel;
import com.paranmanzang.roomservice.model.domain.TimeSaveModel;

public interface TImeService {
    Boolean saveList(TimeSaveModel model);
    Boolean saveListScheduled();

    Boolean saveBooking(BookingModel model);
    void deleteByRoom(Long roomId);
}
