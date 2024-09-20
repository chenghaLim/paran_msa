package com.paranmanzang.roomservice.model.repository.impl;

import com.paranmanzang.roomservice.model.entity.QReview;
import com.paranmanzang.roomservice.model.entity.Review;
import com.paranmanzang.roomservice.model.repository.custom.ReviewCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Review> findByRoom(Long roomId) {
        QReview review= QReview.review;
        return jpaQueryFactory.selectFrom(review)
                .where(review.room.id.eq(roomId))
                .fetch();
    }
}
