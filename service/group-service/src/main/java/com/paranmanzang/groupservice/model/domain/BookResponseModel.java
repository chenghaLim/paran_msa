package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseModel {
    private Long id;
    private String title;
    private String author;

    private String categoryName;

    private int likeBookCount;

    public static BookResponseModel fromEntity(Book book) {
        return BookResponseModel.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .categoryName(book.getCategoryName())
                .likeBookCount(book.getLike_books() != null ? book.getLike_books().size() : 0)
                .build();
    }
}