package com.genedu.project.controller;

import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class LectureContentController {
    private final LectureService lectureService;

    @GetMapping("/lecture-content/{project_id}")
    public ResponseEntity<LectureContentResponseDTO> getLectureContentByProjectId(
            @PathVariable("project_id") UUID projectId
    ) {
        LectureContentResponseDTO lectureContentResponseDTO = lectureService.getLectureContentByProjectId(projectId);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }

    @PostMapping("/lecture-content")
    public ResponseEntity<LectureContentResponseDTO> creatLectureContentByProjectId(
            @RequestBody LectureContentRequestDTO lectureContentRequestDTO
    ) {
        LectureContentResponseDTO lectureContentResponseDTO = lectureService.createLectureContent(lectureContentRequestDTO);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }

    @PutMapping("/lecture-content/{lesson_content_id}")
    public ResponseEntity<LectureContentResponseDTO> updateLectureContentByProjectId(
            @PathVariable("lesson_content_id") UUID lessonContentId,
            @RequestBody LectureContentRequestDTO lectureContentRequestDTO
    ) {
        LectureContentResponseDTO lectureContentResponseDTO = lectureService.updateLectureContent(lessonContentId, lectureContentRequestDTO);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }
}
