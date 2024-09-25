package com.paranmanzang.groupservice.controller;

import com.paranmanzang.groupservice.model.domain.CategoryModel;
import com.paranmanzang.groupservice.service.impl.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/newcategory")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping
    public ResponseEntity<?> getCategoryList() {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }
}
