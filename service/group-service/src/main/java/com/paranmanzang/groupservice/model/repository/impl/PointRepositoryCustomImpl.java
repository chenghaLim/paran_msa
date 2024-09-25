package com.paranmanzang.groupservice.model.repository.impl;

import com.paranmanzang.groupservice.model.domain.PointDetailResponseModel;
import com.paranmanzang.groupservice.model.domain.PointResponseModel;
import com.paranmanzang.groupservice.model.repository.custom.PointRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.paranmanzang.groupservice.model.entity.QPoint.point1;
import static com.paranmanzang.groupservice.model.entity.QPointDetail.pointDetail;

@RequiredArgsConstructor
public class PointRepositoryCustomImpl implements PointRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PointResponseModel> findPointsByGroupId(Long groupId, Pageable pageable) {
        var ids = queryFactory
                .select(point1.id)
                .from(point1)
                .where(point1.groupId.eq(groupId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PointResponseModel> books = ids.isEmpty() ? List.of() :
                queryFactory
                        .select(Projections.constructor(
                                PointResponseModel.class,
                                point1.id,
                                point1.createAt,
                                point1.detail,
                                point1.point,
                                point1.groupId,
                                Projections.list( // Sub-query로 PointDetails 리스트 조회
                                        Projections.constructor(
                                                PointDetailResponseModel.class,
                                                pointDetail.id,
                                                pointDetail.status,
                                                pointDetail.point,
                                                pointDetail.expirationAt,
                                                pointDetail.transactionAt
                                        )
                                )

                        ))
                        .from(point1)
                        .leftJoin(point1.pointDetails, pointDetail)
                        .where(point1.id.in(ids))
                        .fetch();
        return new PageImpl<>(books, pageable, ids.size());
    }
}
