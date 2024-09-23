package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.entity.Book;
import com.paranmanzang.groupservice.model.entity.LikeBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeBooksRepository extends JpaRepository<LikeBooks,Long> {
    boolean existsByNicknameAndBook(String nickname, Book book);

    List<LikeBooks> findByNickname(String nickname);

}
