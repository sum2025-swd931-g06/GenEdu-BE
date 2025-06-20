package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.flatResponse.FlatLessonContentLessonContentMediaFile;
import com.genedu.content.dto.lessoncontent.LessonContentResponseDTO;
import com.genedu.content.dto.lessoncontentmediafile.LessonContentMediaFileRequestDTO;
import com.genedu.content.mapper.LessonContentMapper;
import com.genedu.content.mapper.LessonContentMediaFileMapper;
import com.genedu.content.model.LessonContent;
import com.genedu.content.model.LessonContentMediaFile;
import com.genedu.content.repository.LessonContentMediaFileRepository;
import com.genedu.content.service.LessonContentMediaFileService;
import com.genedu.content.service.LessonContentService;
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
public class LessonContentMediaFileServiceImpl implements LessonContentMediaFileService {
    private final LessonContentMediaFileRepository lessonContentMediaFileRepository;
    private final LessonContentService lessonContentService;

    @Transactional(readOnly = true)
    @Override
    public List<FlatLessonContentLessonContentMediaFile> getAllLessonContentMediaFiles() {
        return lessonContentMediaFileRepository.findAll()
                .stream()
                .map(LessonContentMediaFileMapper::toFlatDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public LessonContentMediaFile getLessonContentMediaFileEntityById(Long id) {
        validateIdNotNull(id, Constants.ErrorCode.LESSON_CONTENT_MEDIA_FILE_ID_NULL);
        return lessonContentMediaFileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.LESSON_CONTENT_MEDIA_FILE_NOT_FOUND, id));
    }

    @Transactional(readOnly = true)
    @Override
    public FlatLessonContentLessonContentMediaFile getLessonContentMediaFileById(Long id) {
        LessonContentMediaFile lessonContentMediaFile = getLessonContentMediaFileEntityById(id);
        return LessonContentMediaFileMapper.toFlatDTO(lessonContentMediaFile);
    }

    @Transactional(readOnly = true)
    @Override
    public LessonContentResponseDTO getLessonContentMediaFilesByLessonContentId(Long lessonContentId) {
        var mediaFiles = lessonContentMediaFileRepository.findByLessonContentId(lessonContentId)
                .stream()
                .map(LessonContentMediaFileMapper::toDTO)
                .toList();

        LessonContent lessonContent = lessonContentService.getLessonContentEntityById(lessonContentId);
        return LessonContentMapper.toDTOWithMediaFiles(lessonContent, mediaFiles);
    }

    @Override
    public FlatLessonContentLessonContentMediaFile createLessonContentMediaFile(Long lessonContentId, LessonContentMediaFileRequestDTO lessonContentMediaFileRequestDTO) {
        if (lessonContentId == 0) {
            throw new BadRequestException(Constants.ErrorCode.LESSON_CONTENT_ID_REQUIRED);
        }
        if (lessonContentMediaFileRepository.existsByOrderNumberAndLessonContentId(lessonContentMediaFileRequestDTO.orderNumber(), lessonContentId)) {
            throw new BadRequestException(Constants.ErrorCode.DUPLICATED_LESSON_CONTENT_MEDIA_FILE_ORDER);
        }
        if (lessonContentMediaFileRequestDTO.orderNumber() < 0) {
            throw new BadRequestException(Constants.ErrorCode.LESSON_CONTENT_MEDIA_FILE_ORDER_NUMBER_INVALID);
        }

        LessonContent lessonContent = lessonContentService.getLessonContentEntityById(lessonContentId);
        LessonContentMediaFile lessonContentMediaFile = LessonContentMediaFileMapper.toEntity(lessonContentMediaFileRequestDTO, lessonContent);
        try {
            LessonContentMediaFile createdLessonContentMediaFile = lessonContentMediaFileRepository.save(lessonContentMediaFile);
            return LessonContentMediaFileMapper.toFlatDTO(createdLessonContentMediaFile);
        } catch (Exception e) {
            log.error("Failed to create Lesson Content Media File: {}", e.getMessage());
            throw new InternalServerErrorException(Constants.ErrorCode.LESSON_CONTENT_MEDIA_FILE_CREATION_FAILED, e);
        }
    }

    @Override
    public FlatLessonContentLessonContentMediaFile updateLessonContentMediaFile(Long id, LessonContentMediaFileRequestDTO lessonContentMediaFileRequestDTO) {
        LessonContentMediaFile existingMediaFile = getLessonContentMediaFileEntityById(id);
        if (lessonContentMediaFileRepository.existsByOrderNumberAndLessonContentIdAndIdNot(
                lessonContentMediaFileRequestDTO.orderNumber(),
                existingMediaFile.getLessonContent().getId(),
                id)
        ) {
            throw new BadRequestException(Constants.ErrorCode.DUPLICATED_LESSON_CONTENT_MEDIA_FILE_ORDER);
        }

        try {
            existingMediaFile.setOrderNumber(lessonContentMediaFileRequestDTO.orderNumber());
            existingMediaFile.setMediaFileId(lessonContentMediaFileRequestDTO.mediaFileId());
            existingMediaFile.setDescription(lessonContentMediaFileRequestDTO.description());

            LessonContentMediaFile updatedMediaFile = lessonContentMediaFileRepository.save(existingMediaFile);
            return LessonContentMediaFileMapper.toFlatDTO(updatedMediaFile);
        } catch (Exception e) {
            log.error("Failed to update Lesson Content Media File: {}", e.getMessage());
            throw new InternalServerErrorException(Constants.ErrorCode.LESSON_CONTENT_MEDIA_FILE_UPDATE_FAILED, e);
        }
    }

    @Override
    public void deleteLessonContentMediaFile(Long id) {
        validateIdNotNull(id, Constants.ErrorCode.LESSON_CONTENT_MEDIA_FILE_ID_NULL);

        if (!lessonContentMediaFileRepository.existsById(id)) {
            throw new NotFoundException(Constants.ErrorCode.LESSON_CONTENT_MEDIA_FILE_NOT_FOUND, id);
        }

        try {
            var existingMediaFile = getLessonContentMediaFileEntityById(id);
//            existingMediaFile.setDeleted(true);
        } catch (Exception e) {
            log.error("Failed to delete Lesson Content Media File: {}", e.getMessage());
            throw new InternalServerErrorException(Constants.ErrorCode.LESSON_CONTENT_MEDIA_FILE_DELETION_FAILED, e);
        }
    }

    private void validateIdNotNull(Long id, String errorCode) {
        if (id == null) {
            throw new BadRequestException(errorCode);
        }
    }
}
