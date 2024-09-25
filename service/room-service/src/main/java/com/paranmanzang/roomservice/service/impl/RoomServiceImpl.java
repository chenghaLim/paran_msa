package com.paranmanzang.roomservice.service.impl;

import com.paranmanzang.roomservice.model.domain.*;
import com.paranmanzang.roomservice.model.entity.Room;
import com.paranmanzang.roomservice.model.repository.RoomRepository;
import com.paranmanzang.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final TimeServiceImpl timeService;

    @Override
    public Boolean save(RoomModel model) {
        return roomRepository.save(Room.builder()
                .name(model.getName())
                .price(model.getPrice())
                .maxPeople(model.getMaxPeople())
                .opened(model.isOpened())
                .openTime(model.getOpenTime())
                .closeTime(model.getCloseTime())
                .nickname(model.getNickname())
                .build()) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Boolean update(RoomUpdateModel model) {
        if (roomRepository.findById(model.getId()).isEmpty()) return Boolean.FALSE;
        return roomRepository.findById(model.getId()).map(room -> {
            room.setName(model.getName());
            room.setMaxPeople(model.getMaxPeople());
            room.setPrice(model.getPrice());
            room.setOpened(model.isOpened());
            room.setOpenTime(model.getOpenTime());
            room.setCloseTime(model.getCloseTime());
            return roomRepository.save(room);
        }) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Boolean delete(Long id) {
        roomRepository.delete(Room.builder()
                .id(id)
                .build());
        return roomRepository.findById(id).isEmpty();
    }

    @Override
    public Boolean enable(Long id) {
        if (roomRepository.findById(id).get().isEnabled()) return Boolean.FALSE;
        return roomRepository.findById(id).map(room -> {
                    room.setEnabled(true);
                    room.setResponseAt(LocalDateTime.now());
                    return roomRepository.save(room);
                }).map(savedRoom ->
                        timeService.saveList(
                                new TimeSaveModel(savedRoom.getId(), savedRoom.getOpenTime().getHour(), savedRoom.getCloseTime().getHour())
                        ))
                .orElse(false); // id에 해당하는 room이 없는 경우 false를 반환
    }


    @Override
    public Page<?> findAll(Pageable pageable) {
        return roomRepository.findByPagination(pageable);
    }

    @Override
    public Page<RoomModel> findAllEnabled(Pageable pageable) {
        return roomRepository.findByPagination(pageable);
    }
    @Override
    public List<?> getIdAllEnabled() {
        return roomRepository.findAll().stream().filter(Room::isEnabled).map(Room::getId).toList();
    }

    @Override
    public Page<?> findByNickname(String nickname, Pageable pageable) {
        return roomRepository.findByNickname(nickname, pageable);
    }

    @Override
    public RoomWTimeModel findByIdWithTime(Long id) {
        return roomRepository.findById(id).filter(Room::isEnabled).map(room ->
                new RoomWTimeModel(room.getId(), room.getName(), room.getMaxPeople(), room.getPrice(), room.isOpened(),
                        room.getOpenTime(), room.getCloseTime(), room.getCreatedAt(), room.isEnabled(), room.getNickname(),
                        room.getTimes().stream().filter(time-> time.getDate().isAfter(LocalDate.now()))
                                .filter(time -> !time.isEnabled())
                                .map(time ->
                                        new TimeModel(time.getId(), time.getDate().toString(), time.getTime().toString())
                                ).toList())
        ).orElse(null);
    }

    @Override
    public RoomModel findById(Long id) {
        return roomRepository.findById(id).map(room ->
                new RoomModel(room.getId(), room.getName(), room.getMaxPeople(), room.getPrice(), room.isOpened(),
                        room.getOpenTime(), room.getCloseTime(), room.getCreatedAt(), room.isEnabled(), room.getNickname())
        ).orElse(null);
    }

}