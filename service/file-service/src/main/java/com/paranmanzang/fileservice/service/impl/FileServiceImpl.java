package com.paranmanzang.fileservice.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.IOUtils;
import com.paranmanzang.fileservice.model.domain.FileDeleteModel;
import com.paranmanzang.fileservice.model.domain.FileModel;
import com.paranmanzang.fileservice.model.entity.File;
import com.paranmanzang.fileservice.model.enums.FileType;
import com.paranmanzang.fileservice.model.repository.FileRepository;
import com.paranmanzang.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final AmazonS3Client objectStorageClient;
    private final String BUCKET_NAME = "paran-test";

    @Override
    public List<?> uploadFile(MultipartFile[] files, String type, Long refId) {
        return Arrays.stream(files).map(file -> {
            String folderName = type + "s/";
            String fileName = file.getOriginalFilename();
            String uploadName = folderName + UUID.randomUUID() + Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));

            return Optional.of(new PutObjectRequest(BUCKET_NAME, folderName, new ByteArrayInputStream(new byte[0]), new ObjectMetadata()))
                    .map(this::createFolder)
                    .map(__ -> uploadFileToStorage(file, uploadName))
                    .map(__ -> saveFileMetadata(uploadName, refId, type))
                    .map(this::convertToFileModel)
                    .orElseThrow(() -> new RuntimeException("Failed to upload file"));
        }).toList();

    }

    private PutObjectResult createFolder(PutObjectRequest request) {
        try {
            return objectStorageClient.putObject(request);
        } catch (SdkClientException e) {
            throw new RuntimeException("Failed to create folder: " + e.getMessage(), e);
        }
    }

    private PutObjectResult uploadFileToStorage(MultipartFile file, String uploadName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            return objectStorageClient.putObject(BUCKET_NAME, uploadName, file.getInputStream(), metadata);
        } catch (IOException | SdkClientException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    private File saveFileMetadata(String uploadName, Long refId, String type) {
        return Optional.of(File.builder()
                        .path(uploadName)
                        .refId(refId)
                        .type(FileType.fromType(type).getCode())
                        .uploadAt(LocalDateTime.now())
                        .build())
                .map(file -> reactiveMongoTemplate.insert(file).block())
                .orElseThrow(() -> new RuntimeException("Failed to save file metadata"));
    }

    @Override
    public List<?> getPathList(List<Long> refIdList, String type) {
        return refIdList.stream().map(refId ->
                        fileRepository.findByRefId(refId, FileType.fromType(type).getCode())
                                .map(this::convertToFileModel)
                                .defaultIfEmpty(FileModel.builder().path(type+"s/default.png").type(type).refId(refId).build())
                                .collectList().block()).toList();// 파일을 FileModel로 변환

    }

    @Override
    public byte[] getFile(String path) throws IOException {
        return IOUtils.toByteArray(objectStorageClient
                .getObject(BUCKET_NAME, path)
                .getObjectContent());
    }

    @Override
    public Boolean delete(FileDeleteModel model) {
        objectStorageClient.deleteObject(BUCKET_NAME, model.getPath());
        fileRepository.deleteByPath(model.getPath()).block();
        return Boolean.TRUE;
    }

    private FileModel convertToFileModel(File file) {
        return Optional.ofNullable(file)
                .map(f -> FileModel.builder()
                        .id(f.getId())
                        .path(f.getPath())
                        .refId(f.getRefId())
                        .type(FileType.fromCode(f.getType()).getType())
                        .uploadAt(f.getUploadAt())
                        .build())
                .orElseThrow(() -> new RuntimeException("Failed to convert to FileModel"));
    }
}
