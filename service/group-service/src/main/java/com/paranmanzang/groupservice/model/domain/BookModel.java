package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Book;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class BookModel {

    @NotBlank(message = "도서명은 필수값입니다.")
    private String title;
    @NotBlank(message = "글쓴이를 입력해주세요.")
    private String author;
    @NotBlank(message = "출판사를 입력해주세요.")
    private String publisher;
    @NotBlank(message = "카테고리명은 필수값입니다.")
    private String categoryName;


    public Book toEntity() {
        return Book.builder()
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .categoryName(this.categoryName)
                .build();
    }
}
