package com.paranmanzang.roomservice.service.impl;

import com.paranmanzang.roomservice.model.domain.ReviewModel;
import com.paranmanzang.roomservice.model.domain.ReviewUpdateModel;
import com.paranmanzang.roomservice.model.entity.Review;
import com.paranmanzang.roomservice.model.repository.BookingRepository;
import com.paranmanzang.roomservice.model.repository.ReviewRepository;
import com.paranmanzang.roomservice.model.repository.RoomRepository;
import com.paranmanzang.roomservice.model.repository.impl.ReviewRepositoryImpl;
import com.paranmanzang.roomservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewRepositoryImpl reviewRepositoryImpl;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Boolean save(ReviewModel model) {
        return reviewRepository.save(Review.builder()
                .content(model.getContent())
                .rating(model.getRating())
                .nickname(model.getNickname())
                .room(roomRepository.findById(model.getRoomId()).get())
                .booking(bookingRepository.findById(model.getBookingId()).get())
                .build()) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean update(ReviewUpdateModel model) {
        if (reviewRepository.findById(model.getId()).isEmpty()) return Boolean.FALSE;

        return reviewRepository.findById(model.getId()).map(review -> {
            review.setRating(model.getRating());
            review.setContent(model.getContent());
            return reviewRepository.save(review);
        }) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Boolean delete(Long id) {
        reviewRepository.delete(Review.builder().id(id).build());
        return reviewRepository.findById(id).isEmpty();
    }

    @Override
    public List<?> findByRoom(Long roomId) {
        return reviewRepositoryImpl.findByRoom(roomId).stream().map(review ->
                new ReviewModel(review.getId(), review.getRating(), review.getContent(), review.getNickname(),
                        review.getCreateAt(), review.getRoom().getId(), review.getBooking().getId())
        ).toList();
    }

    @Override
    public ReviewModel findById(Long id) {
        return reviewRepository.findById(id).map(review ->
                new ReviewModel(review.getId(), review.getRating(), review.getContent(), review.getNickname(),
                        review.getCreateAt(), review.getRoom().getId(), review.getBooking().getId())
        ).orElse(null);
    }

    @Override
    public List<?> findAll() {
        return reviewRepository.findAll().stream().map(review ->
                new ReviewModel(review.getId(), review.getRating(), review.getContent(), review.getNickname(),
                        review.getCreateAt(), review.getRoom().getId(), review.getBooking().getId())
        ).toList();
    }
}
