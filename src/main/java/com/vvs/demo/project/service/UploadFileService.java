package com.vvs.demo.project.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    String upload(MultipartFile file);
}
