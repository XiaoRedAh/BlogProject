package com.xiaored.service;

import com.xiaored.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
