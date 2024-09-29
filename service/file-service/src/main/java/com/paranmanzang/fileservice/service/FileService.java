package com.paranmanzang.fileservice.service;

import com.paranmanzang.fileservice.model.domain.FileDeleteModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<?> uploadFile(MultipartFile[] file, String type, Long refId);
    List<?> getPathList(List<Long> refIdList, String type);
    byte[] getFile(String path) throws IOException;
    Boolean delete(FileDeleteModel model);
}


