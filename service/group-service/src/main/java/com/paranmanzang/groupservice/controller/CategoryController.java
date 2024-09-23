package com.paranmanzang.groupservice.controller;

import com.paranmanzang.groupservice.model.domain.CategoryModel;
import com.paranmanzang.groupservice.service.impl.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/newcategory")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {
    private CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("")
    public ResponseEntity<?> addcategory(@Valid @RequestBody CategoryModel categoryModel,
                                         BindingResult bindingResult) throws BindException {
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(categoryService.addcategory(categoryModel));
    }
}
