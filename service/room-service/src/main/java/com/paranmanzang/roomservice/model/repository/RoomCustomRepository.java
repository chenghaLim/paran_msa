package com.paranmanzang.roomservice.model.repository;


import com.paranmanzang.roomservice.model.domain.RoomModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomCustomRepository {
    Page<?> findByNickname( String nickname, Pageable pageable);
    Page<RoomModel> findByPagination(Pageable pageable);
    List<Long> findAllByNickname(String nickname);
}
