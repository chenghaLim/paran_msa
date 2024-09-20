package com.paranmanzang.roomservice.service.impl;

import com.paranmanzang.roomservice.model.domain.BookingModel;
import com.paranmanzang.roomservice.model.domain.TimeSaveModel;
import com.paranmanzang.roomservice.model.entity.Time;
import com.paranmanzang.roomservice.model.repository.RoomRepository;
import com.paranmanzang.roomservice.model.repository.TimeRepository;
import com.paranmanzang.roomservice.model.repository.impl.TimeRepositoryImpl;
import com.paranmanzang.roomservice.service.TImeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TImeService {
    private final TimeRepository timeRepository;
    private final TimeRepositoryImpl timeRepositoryCustom;
    private final RoomRepository roomRepository;

    @Override
    public Boolean saveList(TimeSaveModel model) {
        return  timeRepository.saveAll(LocalDate.now().datesUntil(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).plusDays(1))
                .flatMap(date -> IntStream.rangeClosed(model.getOpenTime(), model.getCloseTime())
                        .mapToObj(hour ->
                                Time.builder().date(date).time(LocalTime.of(hour, 0)).room(roomRepository.findById(model.getRoomId()).get()).build()
                        )).collect(Collectors.toList()))
                .isEmpty();
    }

//    @Scheduled(fixedDelay = 3000000)
    @Override
    public Boolean saveListScheduled() {
        return roomRepository.findAll().parallelStream().map(room ->
                saveList(new TimeSaveModel(room.getId(), room.getOpenTime().getHour(), room.getCloseTime().getHour()))
        ).toList().isEmpty();
    }

    @Override
    public Boolean saveBooking(BookingModel model) {
        return !timeRepository.saveAll(
                timeRepositoryCustom.findByBooking(model).stream()
                        .peek(time -> time.setEnabled(!time.isEnabled()))
                        .toList()
        ).isEmpty();
    }

    @Override
    public void deleteByRoom(Long roomId) {
        timeRepositoryCustom.deleteByRoom(roomId);
    }


}
