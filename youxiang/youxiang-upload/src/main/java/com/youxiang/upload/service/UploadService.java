package com.youxiang.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String upload(MultipartFile file);
}
