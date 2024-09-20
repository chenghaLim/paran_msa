
package com.paranmanzang.roomservice.controller;

import com.paranmanzang.roomservice.model.domain.RoomModel;
import com.paranmanzang.roomservice.model.domain.RoomUpdateModel;
import com.paranmanzang.roomservice.service.impl.RoomServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/rooms/rooms")
public class RoomController {

    private final RoomServiceImpl roomService;

    @PostMapping("/add")
    public ResponseEntity<?> insert(@Valid @RequestBody RoomModel model, BindingResult result)
            throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        return ResponseEntity.ok(roomService.save(model));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody RoomUpdateModel model, BindingResult result)
            throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        return ResponseEntity.ok(roomService.update(model));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.delete(id));
    }

    @GetMapping("/list/{nickname}")
    public ResponseEntity<?> findByUser(@PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(roomService.findByNickname(nickname));

    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.findByIdWithTime(id));
    }

    @PutMapping("/adminAnswer/{id}")
    public ResponseEntity<?> confirm(@PathVariable() Long id) {
        return ResponseEntity.ok(roomService.enable(id));
    }

    @DeleteMapping("/adminAnswer/{id}")
    public ResponseEntity<?> reject(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.delete(id));
    }

}

