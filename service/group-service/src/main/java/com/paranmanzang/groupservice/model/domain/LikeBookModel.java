package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.LikeBooks;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeBookModel {
    private long id;
    @NotBlank(message = "닉네임은 필수 값입니다..")
    private String nickname;
    @NotBlank(message = "도서 아이디는 필수값입니다.")
    private long bookId;

    private String bookName;
    public static LikeBookModel formEntity(LikeBooks likeBooks){
        return LikeBookModel.builder()
                .id(likeBooks.getId())
                .nickname(likeBooks.getNickname())
                .bookName(likeBooks.getBook().getTitle()).build();
    }
}
