package com.genedu.media.controller;

import com.genedu.media.service.LectureMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lecture")
public class LectureFileController {

    LectureMediaService lectureMediaService;

    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> uploadLectureFile(
            @RequestParam("lectureId") String lectureId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return new ResponseEntity<>(lectureMediaService.uploadLectureFile(lectureId, file.getName(), file), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> downloadLectureFile(String lectureId, String fileName) {

        return new ResponseEntity<>("File downloaded successfully", HttpStatus.OK);
    }
}
