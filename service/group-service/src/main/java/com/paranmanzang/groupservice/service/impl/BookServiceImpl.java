package com.paranmanzang.groupservice.service.impl;

import com.paranmanzang.groupservice.model.domain.BookModel;
import com.paranmanzang.groupservice.model.domain.BookResponseModel;
import com.paranmanzang.groupservice.model.domain.ErrorField;
import com.paranmanzang.groupservice.model.entity.Book;
import com.paranmanzang.groupservice.model.entity.Category;
import com.paranmanzang.groupservice.model.repository.BookRepository;
import com.paranmanzang.groupservice.model.repository.CategoryRepository;
import com.paranmanzang.groupservice.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;

    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public Boolean duplicatename(String bookname) {
        return bookRepository.existsByTitle(bookname);
    }

    //#93. 도서명으로 카테고리 조회
    public Object searchCategoryOfBook(String booktitle) {
        Optional<Book> book = bookRepository.findByTitle(booktitle);
        if (!book.isPresent()) {
            return new ErrorField(booktitle, "도서가 존재하지 않습니다.");
        }
        Book bookCategorySearch = book.get();
        return bookCategorySearch.getCategoryName();
    }

    //#91.책 등록
    @Transactional
    public Object addbook(BookModel bookModel) {

        if (duplicatename(bookModel.getTitle())) {
            return new ErrorField(bookModel.getTitle(),
                    "중복되는 도서명입니다.");
        }
        Category bookCategory;
        if (categoryRepository.existsByName(bookModel.getCategoryName())) {
            bookCategory = categoryRepository.findByName(bookModel.getCategoryName());
        } else {//새 카테고리 추가의 경우
            Category newCategory = new Category();
            newCategory.setName(bookModel.getCategoryName());
            bookCategory = categoryRepository.save(newCategory);
        }
        bookModel.setTitle(bookModel.getTitle());
        bookModel.setAuthor(bookModel.getAuthor());
        bookModel.setPublisher(bookModel.getPublisher());
        bookModel.setCategoryName(bookCategory.getName());

        return bookRepository.save(bookModel.toEntity()) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    public Object findOneById(Long bookId) {
        return bookRepository.findById(bookId).isPresent() ? BookResponseModel.fromEntity(bookRepository.findById(bookId).get()) : Boolean.FALSE;
    }
}
