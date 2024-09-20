package com.paranmanzang.fileservice.service;

import com.paranmanzang.fileservice.model.domain.FileDeleteModel;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface FileService {
    Boolean uploadFile(MultipartFile file, String type, Long refId);
    Flux<?> getPathList(Long refId, String type);
    byte[] getFile(String path) throws IOException;
    Boolean delete(FileDeleteModel model);
}
