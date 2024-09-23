package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.model.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    @NotBlank(message = "카테고리명은 필수값입니다.")
    private String name;

    public Category toEntity(){
        return Category.builder()
                .name(this.name)
                .build();
    }
}
