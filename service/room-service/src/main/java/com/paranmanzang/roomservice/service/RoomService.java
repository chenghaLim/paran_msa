package com.paranmanzang.roomservice.service;


import com.paranmanzang.roomservice.model.domain.RoomModel;
import com.paranmanzang.roomservice.model.domain.RoomUpdateModel;
import com.paranmanzang.roomservice.model.domain.RoomWTimeModel;

import java.util.List;

public interface RoomService {
    Boolean save(RoomModel model);

    Boolean update(RoomUpdateModel model);

    Boolean delete(Long id);

    Boolean enable(Long id);
    List<?> findAll();
    List<?> findByNickname(String nickname);

    RoomModel findById(Long id);
    RoomWTimeModel findByIdWithTime(Long id);
}