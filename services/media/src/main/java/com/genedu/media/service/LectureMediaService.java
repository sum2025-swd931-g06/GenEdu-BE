package com.genedu.media.service;

import com.genedu.media.repository.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LectureMediaService {
    private final S3StorageService s3StorageService;

    public String uploadLectureFile(String lectureId, String fileName, MultipartFile file) throws IOException {
        return s3StorageService.upload(lectureId, fileName, file);
    }
}
