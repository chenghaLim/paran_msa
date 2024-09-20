package com.paranmanzang.roomservice.model.repository.custom;

import java.util.List;

public interface RoomCustomRepository {
    List<?> findByNickname( String nickname);
}
