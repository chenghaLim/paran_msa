package com.paranmanzang.roomservice.service;


import com.paranmanzang.roomservice.model.domain.ReviewModel;
import com.paranmanzang.roomservice.model.domain.ReviewUpdateModel;

import java.util.List;

public interface ReviewService {
    Boolean save(ReviewModel model);

    Boolean update(ReviewUpdateModel model);

    Boolean delete(Long id);

    List<?> findByRoom(Long roomId);

    ReviewModel findById(Long id);

    List<?> findAll();
}
