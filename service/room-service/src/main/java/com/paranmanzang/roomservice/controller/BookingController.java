package com.paranmanzang.roomservice.controller;

import com.paranmanzang.roomservice.model.domain.BookingModel;
import com.paranmanzang.roomservice.service.impl.BookingServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;

    @PostMapping("/add")
    public ResponseEntity<?> saveBooking(@Valid @RequestBody BookingModel model, BindingResult result)
            throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        return ResponseEntity.ok(bookingService.save(model));
    }

    @PutMapping("/state/{id}")
    public ResponseEntity<?> saveState(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.updateState(id));
    }

    @DeleteMapping("/state/{id}")
    public ResponseEntity<?> reject(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.delete(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.delete(id));
    }

    @GetMapping("/groups/list/{groupId}")
    public ResponseEntity<?> findByGroupId(@PathVariable("groupId") long groupId) {
        return ResponseEntity.ok(bookingService.findByGroup(groupId));
    }

    @GetMapping("/rooms/list/{roomId}")
    public ResponseEntity<?> findByRoomId(@PathVariable("roomId") long roomId) {
        return ResponseEntity.ok(bookingService.findByRoom(roomId));
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<?> findOne(@PathVariable("id") long id){
        return ResponseEntity.ok(bookingService.findOne(id));
    }
}
