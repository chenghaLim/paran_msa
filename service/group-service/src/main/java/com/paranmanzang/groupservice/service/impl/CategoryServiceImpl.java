package com.paranmanzang.groupservice.service.impl;

import com.paranmanzang.groupservice.model.domain.CategoryModel;
import com.paranmanzang.groupservice.model.repository.CategoryRepository;
import com.paranmanzang.groupservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Object> getCategoryList() {
        return categoryRepository.findAll().stream()
                .map(CategoryModel::fromEntity).collect(Collectors.toList());
    }
}
