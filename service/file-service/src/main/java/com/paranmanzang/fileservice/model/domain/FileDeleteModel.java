package com.paranmanzang.fileservice.model.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileDeleteModel {
    private String path;
}
