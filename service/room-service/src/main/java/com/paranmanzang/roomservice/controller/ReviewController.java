package com.paranmanzang.roomservice.controller;

import com.paranmanzang.roomservice.model.domain.ReviewModel;
import com.paranmanzang.roomservice.model.domain.ReviewUpdateModel;
import com.paranmanzang.roomservice.service.impl.ReviewServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewService;

    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid  @RequestBody ReviewModel reviewModel, BindingResult result) throws BindException {
        if (result.hasErrors()) throw new BindException(result);
        return ResponseEntity.ok(reviewService.save(reviewModel));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody ReviewUpdateModel reviewModel, BindingResult result) throws BindException{
        if (result.hasErrors()) throw new BindException(result);
        return ResponseEntity.ok(reviewService.update(reviewModel));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id){
        return ResponseEntity.ok(reviewService.delete(id));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(reviewService.findAll());
    }

    @GetMapping("/list/rooms/{roomId}")
    public ResponseEntity<?> getListByRoom(@PathVariable("roomId") Long roomId){
        return ResponseEntity.ok(reviewService.findByRoom(roomId));
    }
    @GetMapping("/one/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(reviewService.findById(id));
    }
}
