package com.paranmanzang.groupservice.model.repository.impl;

import com.paranmanzang.groupservice.model.domain.GroupPostResponseModel;
import com.paranmanzang.groupservice.model.repository.custom.GroupPostRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.paranmanzang.groupservice.model.entity.QBook.book;
import static com.paranmanzang.groupservice.model.entity.QGroup.group;
import static com.paranmanzang.groupservice.model.entity.QGroupPost.groupPost;

@RequiredArgsConstructor
public class GroupPostRepositoryCustomImpl implements GroupPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GroupPostResponseModel> findGroupPostByGroupId(Long groupId, Pageable pageable) {
        // Step 1: ID만 조회
        var ids = queryFactory
                .select(groupPost.id)
                .from(groupPost)
                .where(groupPost.group.id.eq(groupId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Step 2: 필요한 필드 조회 및 GroupPostResponseModel 변환
        List<GroupPostResponseModel> content = ids.isEmpty() ? List.of() :
                queryFactory
                        .select(Projections.constructor(
                                GroupPostResponseModel.class,
                                groupPost.id,
                                groupPost.title,
                                groupPost.content,
                                groupPost.createAt,
                                groupPost.modifyAt,
                                groupPost.postCategory,
                                groupPost.viewCount,
                                groupPost.nickname,
                                groupPost.group.id.as("groupId"),
                                groupPost.group.name.as("groupName"),
                                groupPost.book.id.as("bookId"),
                                groupPost.book.title.as("bookTitle")
                        ))
                        .from(groupPost)
                        .leftJoin(groupPost.group, group)
                        .leftJoin(groupPost.book, book)
                        .where(groupPost.id.in(ids))
                        .fetch();

        return new PageImpl<>(content, pageable, ids.size());
    }
}
