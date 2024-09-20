package com.paranmanzang.fileservice.model.repository;

import com.paranmanzang.fileservice.model.repository.custom.FileCustomRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public interface FileRepository extends ReactiveMongoRepository<File, String>, FileCustomRepository {
}
