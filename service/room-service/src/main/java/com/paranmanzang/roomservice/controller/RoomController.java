
package com.paranmanzang.roomservice.controller;

import com.paranmanzang.roomservice.model.domain.RoomModel;
import com.paranmanzang.roomservice.model.domain.RoomUpdateModel;
import com.paranmanzang.roomservice.service.impl.RoomServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "01. Room")
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomServiceImpl roomService;

    @PostMapping("/add")
    @Operation(summary = "공간 등록", description = "공간정보를 db에 저장합니다.", tags = {"01. Room",})
    public ResponseEntity<?> insert(@Valid @RequestBody RoomModel model, BindingResult result)
            throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        return ResponseEntity.ok(roomService.save(model));
    }

    @PutMapping("/update")
    @Operation(summary="공간 수정", description = "공간정보를 수정합니다.")
    public ResponseEntity<?> update(@Valid @RequestBody RoomUpdateModel model, BindingResult result)
            throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        return ResponseEntity.ok(roomService.update(model));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "공간 삭제", description = "id 값에 해당하는 공간정보를 삭제합니다.")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.delete(id));
    }

    @GetMapping("/list/{nickname}")
    @Operation(summary = "등록자의 공간 조회", description = "nickname인 유저가 등록한 모든 공간정보를 조회합니다.")
    public ResponseEntity<?> findByUser(@PathVariable("nickname") String nickname, Pageable pageable) {
        return ResponseEntity.ok(roomService.findByNickname(nickname, pageable));

    }

    @GetMapping("/list")
    @Operation(summary = "공간 조회", description = "등록된 모든 공간정보를 조회합니다.")
    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(roomService.findAll(pageable));
    }

    @GetMapping("/list/Enabled")
    @Operation(summary = "승인된 공간 조회", description = "승인된 모든 공간정보를 조회합니다. ver.pagination")
    public ResponseEntity<?> findAllEnabled(Pageable pageable) {
        return ResponseEntity.ok(roomService.findAllEnabled(pageable));
    }

    @GetMapping("/one/{id}")
    @Operation(summary = "단일 공간 조회", description = "id 값에 해당하는 1건의 공간정보를 조회합니다.")
    public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.findByIdWithTime(id));
    }

    @PutMapping("/adminAnswer/{id}")
    @Operation(summary = "공간 승인", description = "공간 등록이 승인되어 정보가 수정됩니다.")
    public ResponseEntity<?> confirm(@PathVariable() Long id) {
        return ResponseEntity.ok(roomService.enable(id));
    }

    @DeleteMapping("/adminAnswer/{id}")
    @Operation(summary = "공간 거절", description = "공간이 거절되어 정보가 삭제됩니다.")
    public ResponseEntity<?> reject(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.delete(id));
    }

}

