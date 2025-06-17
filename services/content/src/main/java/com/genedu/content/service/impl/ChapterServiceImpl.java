package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.mapper.ChapterMapper;
import com.genedu.content.mapper.SubjectMapper;
import com.genedu.content.model.Chapter;
import com.genedu.content.model.Subject;
import com.genedu.content.repository.ChapterRepository;
import com.genedu.content.service.ChapterService;
import com.genedu.content.service.SubjectService;
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
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final SubjectService subjectService;

    @Transactional(readOnly = true)
    @Override
    public List<FlatSubjectChapterDTO> getAllChapters() {
        return chapterRepository.findAll().stream()
                .map(ChapterMapper::toFlatDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SubjectResponseDTO getChaptersBySubjectId(Long subjectId) {
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        List<ChapterResponseDTO> chapters = chapterRepository.findBySubject_Id(subjectId).stream()
                .map(ChapterMapper::toDTO)
                .toList();
        return SubjectMapper.toDTOWithChapters(subject, chapters);
    }

    @Transactional(readOnly = true)
    @Override
    public FlatSubjectChapterDTO getChapterById(Long id) {
        Chapter chapter = getChapterEntityById(id);
        return ChapterMapper.toFlatDTO(chapter);
    }

    @Transactional(readOnly = true)
    @Override
    public Chapter getChapterEntityById(Long id) {
        validateChapterId(id);
        return chapterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CHAPTER_NOT_FOUND, id));
    }

    @Transactional(readOnly = true)
    @Override
    public ChapterResponseDTO getChapterBySubjectIdAndOrderNumber(Long subjectId, int orderNumber) {
        Chapter chapter = getChapterEntityBySubjectIdAndOrderNumber(subjectId, orderNumber);
        return ChapterMapper.toDTO(chapter);
    }

    @Transactional(readOnly = true)
    @Override
    public Chapter getChapterEntityBySubjectIdAndOrderNumber(Long subjectId, int orderNumber) {
        if (subjectId == null) {
            throw new BadRequestException(Constants.ErrorCode.CHAPTER_ID_REQUIRED);
        }
        if (orderNumber <= 0) {
            throw new BadRequestException(Constants.ErrorCode.DUPLICATED_CHAPTER_ORDER, orderNumber);
        }
        return chapterRepository.findBySubject_IdAndOrderNumber(subjectId, orderNumber)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CHAPTER_NOT_FOUND, subjectId, orderNumber));
    }

    @Override
    public FlatSubjectChapterDTO createChapter(Long subjectId, ChapterRequestDTO chapterRequestDTO) {
        if (chapterRepository.existsByOrOrderNumberAndSubject_Id(chapterRequestDTO.orderNumber(), subjectId)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_CHAPTER_ORDER, chapterRequestDTO.orderNumber());
        }
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        Chapter chapter = ChapterMapper.toEntity(chapterRequestDTO, subject);

        try {
            Chapter savedChapter = chapterRepository.save(chapter);
            return ChapterMapper.toFlatDTO(savedChapter);
        } catch (Exception e) {
            log.error("Error creating chapter", e);
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_CHAPTER_FAILED, e.getMessage());
        }
    }

    @Override
    public FlatSubjectChapterDTO updateChapter(Long id, ChapterRequestDTO chapterRequestDTO) {
        Chapter existingChapter = getChapterEntityById(id);

        if (chapterRepository.existsByOrderNumberAndSubject_IdAndIdNot(
                chapterRequestDTO.orderNumber(), existingChapter.getSubject().getId(), id)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_CHAPTER_ORDER, chapterRequestDTO.orderNumber());
        }

        existingChapter.setOrderNumber(chapterRequestDTO.orderNumber());
        existingChapter.setTitle(chapterRequestDTO.title());
        existingChapter.setDescription(chapterRequestDTO.description());

        try {
            Chapter updatedChapter = chapterRepository.save(existingChapter);
            return ChapterMapper.toFlatDTO(updatedChapter);
        } catch (Exception e) {
            log.error("Error updating chapter", e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_CHAPTER_FAILED, e.getMessage());
        }
    }

    @Override
    public void deleteChapter(Long id) {
        validateChapterId(id);

        if (!chapterRepository.existsById(id)) {
            throw new NotFoundException(Constants.ErrorCode.CHAPTER_NOT_FOUND, id);
        }

        try {
            chapterRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting chapter", e);
            throw new InternalServerErrorException(Constants.ErrorCode.DELETE_CHAPTER_FAILED, e.getMessage());
        }
    }

    // --- Private helpers ---
    private void validateChapterId(Long id) {
        if (id == null) {
            throw new BadRequestException(Constants.ErrorCode.CHAPTER_ID_REQUIRED);
        }
    }
}