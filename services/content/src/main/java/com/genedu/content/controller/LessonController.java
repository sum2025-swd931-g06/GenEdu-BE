package com.genedu.content.controller;

import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterLessonDTO;
import com.genedu.content.dto.lesson.LessonRequestDTO;
import com.genedu.content.service.LessonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents")
@Tag(name = "Lesson", description = "Manage lessons in the system")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/lessons")
    public ResponseEntity<List<FlatSubjectChapterLessonDTO>> getAllLessons() {
        log.info("Fetching all lessons");
        List<FlatSubjectChapterLessonDTO> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/lessons/{id}")
    public ResponseEntity<FlatSubjectChapterLessonDTO> getLessonById(@PathVariable Long id) {
        log.info("Fetching lesson with ID: {}", id);
        FlatSubjectChapterLessonDTO lesson = lessonService.getLessonById(id);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/chapters/{chapterId}/lessons")
    public ResponseEntity<ChapterResponseDTO> getAllLessonsByChapterId(@PathVariable Long chapterId) {
        log.info("Fetching all lessons for chapter ID: {}", chapterId);
        ChapterResponseDTO lessons = lessonService.getChapterLessonsById(chapterId);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/chapters/{chapterId}/lessons")
    public ResponseEntity<FlatSubjectChapterLessonDTO> createLesson(
            @PathVariable Long chapterId,
            @Valid @RequestBody LessonRequestDTO lessonRequestDTO) {
        log.info("Creating lesson for chapter ID: {}", chapterId);
        FlatSubjectChapterLessonDTO createdLesson = lessonService.createLesson(chapterId, lessonRequestDTO);
        return ResponseEntity.created(URI.create("/api/v1/lessons/" + createdLesson.lessonId()))
                .body(createdLesson);
    }

    @PutMapping("/lessons/{id}")
    public ResponseEntity<FlatSubjectChapterLessonDTO> updateLesson(
            @PathVariable Long id,
            @Valid @RequestBody LessonRequestDTO lessonRequestDTO) {
        log.info("Updating lesson with ID: {}", id);
        FlatSubjectChapterLessonDTO updatedLesson = lessonService.updateLesson(id, lessonRequestDTO);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        log.info("Deleting lesson with ID: {}", id);
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
