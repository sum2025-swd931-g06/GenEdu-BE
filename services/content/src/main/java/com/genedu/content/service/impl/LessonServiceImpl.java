package com.genedu.content.service.impl;

import com.genedu.commonlibrary.enumeration.LessonStatus;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.client.LectureContentRequestDTO;
import com.genedu.content.dto.flatResponse.FlatChapterLessonDTO;
import com.genedu.content.dto.lesson.LessonEntityResponseDTO;
import com.genedu.content.dto.lesson.LessonRequestDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.mapper.ChapterMapper;
import com.genedu.content.mapper.LessonMapper;
import com.genedu.content.model.*;
import com.genedu.content.repository.LessonRepository;
import com.genedu.content.service.ChapterService;
import com.genedu.content.service.LessonContentService;
import com.genedu.content.service.LessonService;
import com.genedu.content.service.webclient.LectureContentClient;
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
    public List<FlatChapterLessonDTO> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(LessonMapper::toFlatDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public FlatChapterLessonDTO getLessonById(Long lessonId) {
        var lesson = getLessonEntityById(lessonId);
        return LessonMapper.toFlatDTO(lesson);
    }

    @Override
    public ChapterResponseDTO getLessonsByChapterId(Long chapterId) {
        log.info("Fetching chapter with lessons for chapter ID: {}", chapterId);
        var lessons = lessonRepository.findByChapterId(chapterId)
                .stream()
                .map(LessonMapper::toDTO)
                .toList();

        Chapter chapter = chapterService.getChapterEntityById(chapterId);
        return ChapterMapper.toDTOWithLessons(chapter, lessons);
    }

    @Transactional(readOnly = true)
    @Override
    public Lesson getLessonEntityById(Long lessonId) {
        validateLessonId(lessonId);
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.LESSON_NOT_FOUND, lessonId));
    }

    @Override
    public FlatChapterLessonDTO createLesson(Long chapterId, LessonRequestDTO lessonRequestDTO) {
        if (lessonRepository.existsByChapter_IdAndOrderNumber(chapterId, lessonRequestDTO.orderNumber())) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_LESSON_ORDER, lessonRequestDTO.orderNumber());
        }
        Chapter chapter = chapterService.getChapterEntityById(chapterId);
        Lesson lesson = LessonMapper.toEntity(lessonRequestDTO, chapter);

        try {
            Lesson savedLesson = lessonRepository.save(lesson);
            return LessonMapper.toFlatDTO(savedLesson);
        } catch (Exception e) {
            log.error("Error creating lesson", e);
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_LESSON_FAILED, e.getMessage());
        }
    }

    @Override
    public FlatChapterLessonDTO updateLesson(Long lessonId, LessonRequestDTO lessonRequestDTO) {
        Lesson existingLesson = getLessonEntityById(lessonId);

        if (lessonRepository.existsByChapter_IdAndOrderNumberAndIdNot(
                existingLesson.getChapter().getId(),
                lessonRequestDTO.orderNumber(),
                lessonId)
        ) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_LESSON_ORDER, lessonRequestDTO.orderNumber());
        }

        try {
            existingLesson.setTitle(lessonRequestDTO.title());
            existingLesson.setDescription(lessonRequestDTO.description());
            existingLesson.setOrderNumber(lessonRequestDTO.orderNumber());

            Lesson updatedLesson = lessonRepository.save(existingLesson);
            return LessonMapper.toFlatDTO(updatedLesson);
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
            var existingLesson = getLessonEntityById(lessonId);
            existingLesson.setDeleted(true);
        } catch (Exception e) {
            log.error("Error deleting lesson", e);
            throw new InternalServerErrorException(Constants.ErrorCode.DELETE_LESSON_FAILED, e.getMessage());
        }
    }

    @Override
    public LessonEntityResponseDTO getFlatLessonEntityById(Long lessonId) {
        if (lessonId == null) {
            throw new BadRequestException(Constants.ErrorCode.LESSON_ID_REQUIRED);
        }
        Lesson lesson = lessonRepository.findLessonById(lessonId);
        Chapter chapter = lesson.getChapter();
        Material material = chapter.getMaterial();
        Subject subject = material.getSubject();
        SchoolClass schoolClass = subject.getSchoolClass();

        String schoolClassId = String.valueOf(schoolClass.getId());
        String subjectId = String.valueOf(subject.getId());
        String materialId = String.valueOf(material.getId());
        String chapterId = String.valueOf(chapter.getId());
        String lessonIdStr = String.valueOf(lesson.getId());

        return new LessonEntityResponseDTO(
                schoolClassId,
                subjectId,
                materialId,
                chapterId,
                lessonIdStr
        );
    }

    // --- Private helpers ---
    private void validateLessonId(Long lessonId) {
        if (lessonId == null) {
            throw new BadRequestException(Constants.ErrorCode.LESSON_ID_REQUIRED);
        }
    }
}