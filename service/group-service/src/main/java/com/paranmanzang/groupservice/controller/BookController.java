package com.paranmanzang.groupservice.controller;


import com.paranmanzang.groupservice.service.impl.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/books")
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookService;

    @GetMapping
    public ResponseEntity<?> getBookList(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(bookService.getBookList(PageRequest.of(page, size)));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> findOneByBookId(@PathVariable("bookId") Long id){
        return ResponseEntity.ok(bookService.findOneById(id));
    }
}
