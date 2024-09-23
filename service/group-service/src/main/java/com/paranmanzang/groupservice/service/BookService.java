package com.paranmanzang.groupservice.service;


import com.paranmanzang.groupservice.model.domain.BookModel;

public interface BookService {
    Object addbook(BookModel bookModel);

    Object findOneById(Long bookId);
}