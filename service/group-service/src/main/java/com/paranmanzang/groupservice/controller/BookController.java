package com.paranmanzang.groupservice.controller;


import com.paranmanzang.groupservice.model.domain.BookModel;
import com.paranmanzang.groupservice.service.impl.BookServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {
    private BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    //#93.도서명으로 카테고리 조회
    @GetMapping("/category")
    public ResponseEntity<?> addcategory(@RequestParam String bookTitle) {
        return ResponseEntity.ok(bookService.searchCategoryOfBook(bookTitle));
    }
    //#91.
    @PostMapping("/add")
    public ResponseEntity<?> addbook(@Valid @RequestBody BookModel bookModel,
                                     BindingResult bindingResult) throws BindException {
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(bookService.addbook(bookModel));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> findOneByBookId(@PathVariable("bookId") Long id){
        return ResponseEntity.ok(bookService.findOneById(id));
    }
}
