package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.flatResponse.FlatLessonLessonContentDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.dto.lessoncontent.LessonContentRequestDTO;
import com.genedu.content.dto.lessoncontent.LessonContentResponseDTO;
import com.genedu.content.mapper.LessonContentMapper;
import com.genedu.content.mapper.LessonMapper;
import com.genedu.content.model.Lesson;
import com.genedu.content.model.LessonContent;
import com.genedu.content.repository.LessonContentRepository;
import com.genedu.content.service.LessonContentService;
import com.genedu.content.service.LessonService;
import com.genedu.content.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class LessonContentServiceImpl implements LessonContentService {

    private final LessonContentRepository lessonContentRepository;
    private final LessonService lessonService;

    @Transactional(readOnly = true)
    @Override
    public List<FlatLessonLessonContentDTO> getAllLessonContents() {
        return lessonContentRepository.findAll().stream()
                .map(LessonContentMapper::toFlatDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public LessonResponseDTO getLessonContentsByLessonId(Long lessonId) {
        Lesson lesson = lessonService.getLessonEntityById(lessonId);
        var lessonContents = lessonContentRepository.findByLessonId(lessonId)
                .stream()
                .map(LessonContentMapper::toDTO)
                .toList();
        return LessonMapper.toDTOWithLessonContent(lesson, lessonContents);
    }

    @Transactional(readOnly = true)
    @Override
    public LessonResponseDTO getLessonWithLessonContents(Long lessonId) {
        var lessonContents = lessonContentRepository.findByLessonId(lessonId);
        var lessonContentResponseDTOs = lessonContents.stream()
                .map(LessonContentMapper::toDTO)
                .toList();

        var lessonResponseDTO = lessonService.getLessonEntityById(lessonId);

        return LessonMapper.toDTOWithLessonContent(lessonResponseDTO, lessonContentResponseDTOs);
    }

    @Transactional(readOnly = true)
    @Override
    public FlatLessonLessonContentDTO getLessonContentById(Long id) {
        var lessonContent = getLessonContentEntityById(id);
        return LessonContentMapper.toFlatDTO(lessonContent);
    }

    @Transactional(readOnly = true)
    @Override
    public LessonContent getLessonContentEntityById(Long id) {
        validateLessonContentId(id);
        return lessonContentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.LESSON_CONTENT_NOT_FOUND, id));
    }

    @Transactional(readOnly = true)
    @Override
    public LessonContentResponseDTO getLessonContentByLessonIdAndOrderNumber(Long lessonId, int orderNumber) {
        var lessonContent = getLessonContentEntityByLessonIdAndOrderNumber(lessonId, orderNumber);
        return LessonContentMapper.toDTO(lessonContent);
    }

    @Transactional(readOnly = true)
    @Override
    public LessonContent getLessonContentEntityByLessonIdAndOrderNumber(Long lessonId, int orderNumber) {
        validateLessonContentId(lessonId);
        if (orderNumber < 0) {
            throw new BadRequestException(Constants.ErrorCode.LESSON_CONTENT_ORDER_NUMBER_INVALID, orderNumber);
        }
        return lessonContentRepository.findByLessonIdAndOrderNumber(lessonId, orderNumber)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.LESSON_CONTENT_NOT_FOUND, lessonId, orderNumber));
    }

    @Override
    public FlatLessonLessonContentDTO createLessonContent(Long lessonId, LessonContentRequestDTO lessonContentRequestDTO) {
        if (lessonContentRepository.existsByOrderNumberAndLessonId(lessonContentRequestDTO.orderNumber(), lessonId)) {
            throw new BadRequestException(Constants.ErrorCode.DUPLICATED_LESSON_CONTENT_ORDER, lessonContentRequestDTO.orderNumber());
        }
        var lesson = lessonService.getLessonEntityById(lessonId);
        var lessonContent = LessonContentMapper.toEntity(lessonContentRequestDTO, lesson);

        try {
            LessonContent savedLessonContent = lessonContentRepository.save(lessonContent);
            return LessonContentMapper.toFlatDTO(savedLessonContent);
        } catch (Exception e) {
            log.error("Error creating lesson content: {}", e.getMessage());
            throw new BadRequestException(Constants.ErrorCode.LESSON_CONTENT_CREATION_FAILED, e.getMessage());
        }
    }

    @Override
    public FlatLessonLessonContentDTO updateLessonContent(Long id, LessonContentRequestDTO lessonContentRequestDTO) {
        var existingLessonContent = getLessonContentEntityById(id);
        if(lessonContentRepository.existsByOrderNumberAndLessonIdAndIdNot(
                lessonContentRequestDTO.orderNumber(), existingLessonContent.getLesson().getId(), id)) {
            throw new BadRequestException(Constants.ErrorCode.DUPLICATED_LESSON_CONTENT_ORDER, lessonContentRequestDTO.orderNumber());
        }

        try {
            existingLessonContent.setTitle(lessonContentRequestDTO.title());
            existingLessonContent.setContent(lessonContentRequestDTO.content());
            existingLessonContent.setOrderNumber(lessonContentRequestDTO.orderNumber());

            var updatedLessonContent = lessonContentRepository.save(existingLessonContent);
            return LessonContentMapper.toFlatDTO(updatedLessonContent);
        } catch (Exception e) {
            log.error("Error updating lesson content: {}", e.getMessage());
            throw new InternalServerErrorException(Constants.ErrorCode.LESSON_CONTENT_CREATION_FAILED, e.getMessage());
        }
    }

    @Override
    public void deleteLessonContent(Long id) {
        validateLessonContentId(id);

        if (!lessonContentRepository.existsById(id)) {
            throw new NotFoundException(Constants.ErrorCode.LESSON_CONTENT_NOT_FOUND, id);
        }
        try {
            var existingLessonContent = getLessonContentEntityById(id);
            existingLessonContent.setDeleted(true);
        } catch (Exception e) {
            log.error("Error deleting lesson content: {}", e.getMessage());
            throw new InternalServerErrorException(Constants.ErrorCode.LESSON_CONTENT_CREATION_FAILED, e.getMessage());
        }
    }

    private void validateLessonContentId(Long id) {
        if (id == null) {
            throw new BadRequestException(Constants.ErrorCode.LESSON_CONTENT_ID_REQUIRED);
        }
    }
}
