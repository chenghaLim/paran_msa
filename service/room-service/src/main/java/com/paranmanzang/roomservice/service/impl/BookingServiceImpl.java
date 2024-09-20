package com.paranmanzang.roomservice.service.impl;

import com.paranmanzang.roomservice.model.domain.BookingModel;
import com.paranmanzang.roomservice.model.entity.Booking;
import com.paranmanzang.roomservice.model.repository.BookingRepository;
import com.paranmanzang.roomservice.model.repository.RoomRepository;
import com.paranmanzang.roomservice.model.repository.impl.BookingRepositoryImpl;
import com.paranmanzang.roomservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingRepositoryImpl bookingCustomRepository;
    private final RoomRepository roomRepository;
    private final TimeServiceImpl timeService;
    private final AccountServiceImpl accountService;

    @Override
    public Boolean save(BookingModel model) {
        bookingRepository.save(Booking.builder()
                .usingStart(model.getUsingStart())
                .usingEnd(model.getUsingEnd())
                .groupId(model.getGroupId())
                .room(roomRepository.findById(model.getRoomId()).get())
                .build());
        return timeService.saveBooking(model);
    }

    @Override
    public Boolean delete(Long id) {
        var booking = bookingRepository.findById(id).get();
        bookingRepository.delete(booking);
        return bookingRepository.findById(id).isEmpty() && timeService.saveBooking(
                new BookingModel(booking.getId(), booking.isEnabled(), booking.getUsingStart(), booking.getUsingEnd(), booking.getRoom().getId(), booking.getGroupId())
        )&& accountService.cancel(id);
    }

    @Override
    public List<?> findByGroup(long groupId) {
        return bookingCustomRepository.findByGroupId(groupId).stream().map(booking ->
                new BookingModel(booking.getId(), booking.isEnabled(), booking.getUsingStart(),
                        booking.getUsingEnd(), booking.getRoom().getId(), booking.getGroupId())
        ).toList();
    }

    @Override
    public List<?> findByRoom(long roomId) {
        return bookingCustomRepository.findByRoomId(roomId).stream().map(booking ->
                new BookingModel(booking.getId(), booking.isEnabled(), booking.getUsingStart(),
                        booking.getUsingEnd(), booking.getRoom().getId(), booking.getGroupId())
        ).toList();
    }

    @Override
    public BookingModel findOne(long id) {
        return bookingRepository.findById(id).map(booking ->
                new BookingModel(booking.getId(), booking.isEnabled(), booking.getUsingStart(), booking.getUsingEnd(), booking.getRoom().getId(), booking.getGroupId())
        ).orElse(null);
    }


    @Override
    public Boolean updateState(Long id) {
        if (bookingRepository.findById(id).get().isEnabled()) return Boolean.FALSE;

        return bookingRepository.findById(id).map(booking -> {
            booking.setEnabled(true);
            booking.setResponseAt(LocalDateTime.now());
            return bookingRepository.save(booking);
        }) == null ? Boolean.FALSE : Boolean.TRUE;

    }

}
