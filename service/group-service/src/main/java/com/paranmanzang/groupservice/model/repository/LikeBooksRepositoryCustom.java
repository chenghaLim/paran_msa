package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.domain.LikeBookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeBooksRepositoryCustom {
    Page<LikeBookModel> findLikeBooksByNickname(String nickname, Pageable pageable);

}
