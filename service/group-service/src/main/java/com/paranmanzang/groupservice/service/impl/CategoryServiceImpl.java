package com.paranmanzang.groupservice.service.impl;

import com.paranmanzang.groupservice.model.domain.CategoryModel;
import com.paranmanzang.groupservice.model.repository.CategoryRepository;
import com.paranmanzang.groupservice.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Boolean addcategory(CategoryModel categoryModel) {
        return categoryRepository.save(categoryModel.toEntity()) != null ? Boolean.TRUE : Boolean.FALSE;
    }
}
