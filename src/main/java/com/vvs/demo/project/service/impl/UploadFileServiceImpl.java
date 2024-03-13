package com.vvs.demo.project.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.vvs.demo.project.exception.FileCopyException;
import com.vvs.demo.project.service.UploadFileService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadFileServiceImpl implements UploadFileService {
    private final AmazonS3 amazonS3;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            Files.copy(file.getInputStream(),
                    convertedFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new FileCopyException("File can`t be copied!");
        }

        amazonS3.putObject(new PutObjectRequest(bucketName,
                file.getOriginalFilename(),
                convertedFile));
        return file.getOriginalFilename();
    }
}
