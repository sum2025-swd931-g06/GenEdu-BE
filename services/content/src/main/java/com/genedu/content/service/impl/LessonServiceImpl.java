package com.genedu.content.service.impl;

import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterLessonDTO;
import com.genedu.content.dto.lesson.LessonRequestDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.mapper.ChapterMapper;
import com.genedu.content.mapper.LessonMapper;
import com.genedu.content.model.Chapter;
import com.genedu.content.model.Lesson;
import com.genedu.content.repository.LessonRepository;
import com.genedu.content.service.ChapterService;
import com.genedu.content.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ChapterService chapterService;

    @Override
    public List<FlatSubjectChapterLessonDTO> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();

        return lessons.stream()
                .map(LessonMapper::toDTOWithChapter)
                .toList();
    }

    @Override
    public List<LessonResponseDTO> getAllLessonsByChapterId(Long chapterId) {
        List<Lesson> lessons = lessonRepository.findAllByChapterId(chapterId);

        return lessons.stream()
                .map(LessonMapper::toDTO)
                .toList();
    }

    @Override
    public ChapterResponseDTO getChapterLessonsById(Long id) {
        Chapter chapter = chapterService.getChapterEntityById(id);
        List<LessonResponseDTO> lessonResponseDTOs = getAllLessonsByChapterId(id);

        return ChapterMapper.toDTOWithLessons(chapter, lessonResponseDTOs);
    }

    @Override
    public FlatSubjectChapterLessonDTO getLessonById(Long lessonId) {
        if (lessonId == null) {
            throw new IllegalArgumentException("Lesson ID cannot be null");
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));

        return LessonMapper.toDTOWithChapter(lesson);
    }

    @Override
    public FlatSubjectChapterLessonDTO createLesson(Long chapterId, LessonRequestDTO lessonRequestDTO) {
        if (lessonRepository.existsByChapter_IdAndOrderNumber(chapterId, lessonRequestDTO.orderNumber())) {
            throw new IllegalArgumentException("A lesson with the same order number already exists in this chapter.");
        }

        Chapter chapter = chapterService.getChapterEntityById(chapterId);
        Lesson lesson = LessonMapper.toEntity(lessonRequestDTO, chapter);

        Lesson savedLesson = lessonRepository.save(lesson);
        return LessonMapper.toDTOWithChapter(savedLesson);
    }

    @Override
    public FlatSubjectChapterLessonDTO updateLesson(Long lessonId, LessonRequestDTO lessonRequestDTO) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));

        if (lessonRepository.existsByChapter_IdAndOrderNumberAndIdNot(existingLesson.getChapter().getId(), lessonRequestDTO.orderNumber(), lessonId)) {
            throw new IllegalArgumentException("A lesson with the same order number already exists in this chapter.");
        }

        existingLesson.setTitle(lessonRequestDTO.title());
        existingLesson.setDescription(lessonRequestDTO.description());
        existingLesson.setOrderNumber(lessonRequestDTO.orderNumber());

        Lesson updatedLesson = lessonRepository.save(existingLesson);
        return LessonMapper.toDTOWithChapter(updatedLesson);
    }

    @Override
    public void deleteLesson(Long lessonId) {
        if (lessonId == null) {
            throw new IllegalArgumentException("Lesson ID cannot be null");
        }

        if (!lessonRepository.existsById(lessonId)) {
            throw new RuntimeException("Lesson not found with id: " + lessonId);
        }

        lessonRepository.deleteById(lessonId);
    }
}
