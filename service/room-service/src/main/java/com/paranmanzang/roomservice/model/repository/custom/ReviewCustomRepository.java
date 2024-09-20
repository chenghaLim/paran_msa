package com.paranmanzang.roomservice.model.repository.custom;

import com.paranmanzang.roomservice.model.entity.Review;

import java.util.List;


public interface ReviewCustomRepository {
    List<Review> findByRoom(Long roomId);
}
