package com.genedu.project.controller;

import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.service.LectureContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class LectureContentController {
    private final LectureContentService lectureContentService;

    @GetMapping("/lecture-content/{project_id}")
    public ResponseEntity<LectureContentResponseDTO> getLectureContentByProjectId(
            @PathVariable("project_id") UUID projectId
    ) {
        LectureContentResponseDTO lectureContentResponseDTO = lectureContentService.getLectureContentByProjectId(projectId);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }

    @PostMapping("/lecture-content}")
    public ResponseEntity<LectureContentResponseDTO> creatLectureContentByProjectId(
            @RequestBody LectureContentRequestDTO lectureContentRequestDTO
    ) {
        LectureContentResponseDTO lectureContentResponseDTO = lectureContentService.createLectureContent(lectureContentRequestDTO);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }

    @PutMapping("/lecture-content/{lesson_content_id}")
    public ResponseEntity<LectureContentResponseDTO> updateLectureContentByProjectId(
            @PathVariable("lesson_content_id") UUID lessonContentId,
            @RequestBody LectureContentRequestDTO lectureContentRequestDTO
    ) {
        LectureContentResponseDTO lectureContentResponseDTO = lectureContentService.updateLectureContent(lessonContentId, lectureContentRequestDTO);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }
}
