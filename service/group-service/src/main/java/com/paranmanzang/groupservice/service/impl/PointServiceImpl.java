package com.paranmanzang.groupservice.service.impl;

import com.paranmanzang.groupservice.model.domain.ErrorField;
import com.paranmanzang.groupservice.model.domain.PointModel;
import com.paranmanzang.groupservice.model.domain.PointResponseModel;
import com.paranmanzang.groupservice.model.entity.Point;
import com.paranmanzang.groupservice.model.entity.PointDetail;
import com.paranmanzang.groupservice.model.repository.PointDetailRepository;
import com.paranmanzang.groupservice.model.repository.PointRepository;
import com.paranmanzang.groupservice.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final PointDetailRepository pointDetailRepository;

    @Transactional
    public Object addPoint(PointModel pointModel) {
        Optional<Point> grouppoint = pointRepository.findByGroupId(pointModel.getGroupId());
        Point point;

        if (grouppoint.isPresent()) {//기존 포인트 갱신
            point = grouppoint.get();
            point.setPoint(point.getPoint() + pointModel.getPoint());


        } else {//신규 저장
            point = new Point();
            point.setPoint(pointModel.getPoint());
            point.setGroupId(pointModel.getGroupId());


        }

        point.setCreateAt(LocalDateTime.now());
        point.setDetail("적립");
        pointRepository.save(point);
        pointDetailRepository.save(new PointDetail("신규적립",
                pointModel.getPoint(),
                LocalDateTime.now(),
                LocalDate.now().plusMonths(3),
                point));
        return Boolean.TRUE;
    }

    public Object usePoint(PointModel pointModel) {//결제
        Optional<Point> grouppoint = pointRepository.findByGroupId(pointModel.getGroupId());
        if (grouppoint.isPresent()) {
            Point beforepoint = grouppoint.get();
            if (beforepoint.getPoint() >= pointModel.getPoint()) { //기존포인트 > 사용하려는 포인트
                beforepoint.setPoint(beforepoint.getPoint() - pointModel.getPoint()); // 결제완료
                pointRepository.save(beforepoint);

                PointDetail newPointDetail = new PointDetail("사용완료", pointModel.getPoint(), LocalDateTime.now(), beforepoint);
                pointDetailRepository.save(newPointDetail);
                return Boolean.TRUE;
            } else {
                return new ErrorField(pointModel.getPointId(), "포인트가 부족합니다.");
            }
        } else {
            return new ErrorField(pointModel.getPointId(), "포인트가 존재하지 않습니다.");
        }
    }

    public Object deletePoint(Long pointId) {//결제 취소
        Optional<PointDetail> optionalPointDetail = pointDetailRepository.findById(pointId);
        if (optionalPointDetail.isPresent()) {
            PointDetail pointDetail = optionalPointDetail.get();
            pointDetailRepository.deleteById(pointId);

            Optional<Point> target = pointRepository.findById(pointDetail.getParentPoint().getId());
            Point targetPoint = target.get();
            targetPoint.setPoint(targetPoint.getPoint() - pointDetail.getPoint());
            pointRepository.save(targetPoint);
            return Boolean.TRUE;
        } else {
            return new ErrorField(pointId, "포인트 이력이 존재하지 않습니다.");
        }
    }

    //@Scheduled(fixedDelay = 86400000L) // 1일마다 실행
    @Scheduled(fixedDelay = 10000L) // 30초마다 실행
    public void scheduledPointExpiration() {
        LocalDate today = LocalDate.now();
        oldpoint(today); // 만료 처리
    }

    @Transactional
    public void oldpoint(LocalDate today) {
        List<PointDetail> expiredPoints = pointDetailRepository.findAll();
        for (PointDetail pointDetail : expiredPoints) {
            LocalDate expirationAt = pointDetail.getExpirationAt();
            if (expirationAt != null && expirationAt.isBefore(today)) { // expirationAt이 null이 아니고 today보다 이전인 경우
                oldPointKill(pointDetail.getId());
            }
        }
    }

    public Object oldPointKill(Long pointId) {//만료
        Optional<PointDetail> optionalPointDetail = pointDetailRepository.findById(pointId);
        if (optionalPointDetail.isPresent()) {
            PointDetail pointDetail = optionalPointDetail.get();
            pointDetail.setStatus("만료");

            pointDetailRepository.save(pointDetail);

            Optional<Point> target = pointRepository.findById(pointDetail.getParentPoint().getId());
            Point targetPoint = target.get();
            targetPoint.setPoint(targetPoint.getPoint() - pointDetail.getPoint());
            pointRepository.save(targetPoint);
            return HttpStatus.OK;
        } else {
            return new ErrorField(pointId, "포인트 이력이 존재하지 않습니다.");
        }
    }

    public Page<?> searchPoint(Long groupId, Pageable pageable) {
        return pointRepository.findPointsByGroupId(groupId,pageable);
    }

}