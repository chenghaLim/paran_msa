package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    Category findByName(String name);

    Category save(Category category);
}
