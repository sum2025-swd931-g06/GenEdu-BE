package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
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
import com.genedu.content.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ChapterService chapterService;

    @Transactional(readOnly = true)
    @Override
    public List<FlatSubjectChapterLessonDTO> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(LessonMapper::toDTOWithChapter)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonResponseDTO> getAllLessonsByChapterId(Long chapterId) {
        return lessonRepository.findAllByChapterId(chapterId).stream()
                .map(LessonMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ChapterResponseDTO getChapterLessonsById(Long id) {
        Chapter chapter = chapterService.getChapterEntityById(id);
        List<LessonResponseDTO> lessonResponseDTOs = getAllLessonsByChapterId(id);
        return ChapterMapper.toDTOWithLessons(chapter, lessonResponseDTOs);
    }

    @Transactional(readOnly = true)
    @Override
    public FlatSubjectChapterLessonDTO getLessonById(Long lessonId) {
        validateLessonId(lessonId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.LESSON_NOT_FOUND, lessonId));
        return LessonMapper.toDTOWithChapter(lesson);
    }

    @Override
    public FlatSubjectChapterLessonDTO createLesson(Long chapterId, LessonRequestDTO lessonRequestDTO) {
        if (lessonRepository.existsByChapter_IdAndOrderNumber(chapterId, lessonRequestDTO.orderNumber())) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_LESSON_ORDER, lessonRequestDTO.orderNumber());
        }
        Chapter chapter = chapterService.getChapterEntityById(chapterId);
        Lesson lesson = LessonMapper.toEntity(lessonRequestDTO, chapter);

        try {
            Lesson savedLesson = lessonRepository.save(lesson);
            return LessonMapper.toDTOWithChapter(savedLesson);
        } catch (Exception e) {
            log.error("Error creating lesson", e);
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_LESSON_FAILED, e.getMessage());
        }
    }

    @Override
    public FlatSubjectChapterLessonDTO updateLesson(Long lessonId, LessonRequestDTO lessonRequestDTO) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.LESSON_NOT_FOUND, lessonId));

        if (lessonRepository.existsByChapter_IdAndOrderNumberAndIdNot(
                existingLesson.getChapter().getId(),
                lessonRequestDTO.orderNumber(),
                lessonId)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_LESSON_ORDER, lessonRequestDTO.orderNumber());
        }

        try {
            existingLesson.setTitle(lessonRequestDTO.title());
            existingLesson.setDescription(lessonRequestDTO.description());
            existingLesson.setOrderNumber(lessonRequestDTO.orderNumber());

            Lesson updatedLesson = lessonRepository.save(existingLesson);
            return LessonMapper.toDTOWithChapter(updatedLesson);
        } catch (Exception e) {
            log.error("Error updating lesson", e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_LESSON_FAILED, e.getMessage());
        }
    }

    @Override
    public void deleteLesson(Long lessonId) {
        validateLessonId(lessonId);
        if (!lessonRepository.existsById(lessonId)) {
            throw new NotFoundException(Constants.ErrorCode.LESSON_NOT_FOUND, lessonId);
        }
        try {
            lessonRepository.deleteById(lessonId);
        } catch (Exception e) {
            log.error("Error deleting lesson", e);
            throw new InternalServerErrorException(Constants.ErrorCode.DELETE_LESSON_FAILED, e.getMessage());
        }
    }

    // --- Private helpers ---
    private void validateLessonId(Long lessonId) {
        if (lessonId == null) {
            throw new BadRequestException(Constants.ErrorCode.LESSON_ID_REQUIRED);
        }
    }
}