package com.paranmanzang.userservice.service.impl;/*
package com.paranmanzang.userservice.service.user.impl;

import com.paranmanzang.userservice.model.domain.user.LikeBookModel;
import com.paranmanzang.userservice.model.entity.group.Book;
import com.paranmanzang.userservice.model.entity.group.likeBooks;
import com.paranmanzang.userservice.model.entity.user.User;
import com.paranmanzang.userservice.model.repository.user.LikeBookRepository;
import com.paranmanzang.userservice.model.repository.user.UserRepository;
import com.paranmanzang.userservice.service.user.LikeBookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeBookServiceImpl implements LikeBookService {
    private final LikeBookRepository likebookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LikeBookServiceImpl(LikeBookRepository likebookRepository,UserRepository userRepository, BookRepository bookRepository){
        this.likebookRepository = likebookRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean add(LikeBookModel likeBookModel) {
        Long userId = likeBookModel.getUserId();
        Long bookId = likeBookModel.getBookId();

        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if(optionalUser.isPresent() && optionalBook.isPresent()) {
            throw new RuntimeException("사용자나 방의 정보가 잘못되었습니다.");
        }

        if (likebookRepository.existsByUserIdAndBookId(userId, bookId)) {
            System.out.println("test");
            return false; // 이미 좋아요를 눌렀으면 false 반환
        }

        likeBooks likebooks = new likeBooks();
        likebooks.setBookId(bookId);
        likebooks.setUser(optionalUser.get());
        likebookRepository.save(likebooks);
        return true;
    }

    @Override
    public boolean remove(LikeBookModel likeBooKModel) {
        likeBooks likebooks = likebookRepository.findByUserIdAndBookId(likeBooKModel.getUserId(), likeBooKModel.getBookId());
        if (likebooks != null) {
            likebookRepository.deleteByUserIdAndBookId(likeBooKModel.getUserId(), likeBooKModel.getBookId());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLikeById(Long id) {
        likebookRepository.deleteById(id);
        System.out.println(!likebookRepository.existsById(id));
        return !likebookRepository.existsById(id);
    }

    @Override
    public List<LikeBookModel> findAll(long userId) {
        List<likeBooks> likebooks = likebookRepository.findByUserId(userId);
        return likebooks.stream()
                .map(likeBook -> new LikeBookModel(likeBook.getId(),
                        likeBook.getBookId(),
                        likeBook.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public LikeBookModel existsByUserIdAndBookId(Long userId, Long bookId) {
        likeBooks likebooks = likebookRepository.findByUserIdAndBookId(userId, bookId);
        if(likebooks != null) {
            System.out.println(new LikeBookModel(likebooks.getId(), likebooks.getBookId(), likebooks.getUserId()));
            return new LikeBookModel(likebooks.getId(), likebooks.getBookId(), likebooks.getUserId());
        }
        System.out.println("null");
        return null;
    }
}
*/
