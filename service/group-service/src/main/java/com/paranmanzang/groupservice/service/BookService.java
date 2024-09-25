package com.paranmanzang.groupservice.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Object findOneById(Long bookId);

    Page<?> getBookList(Pageable pageable);
}