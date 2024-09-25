package com.paranmanzang.groupservice.model.repository.custom;

import com.paranmanzang.groupservice.model.domain.BookResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<BookResponseModel> findBooks(Pageable pageable);
}
