package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatMaterialChapterDTO;
import com.genedu.content.dto.material.MaterialResponseDTO;
import com.genedu.content.mapper.ChapterMapper;
import com.genedu.content.mapper.MaterialMapper;
import com.genedu.content.model.Chapter;
import com.genedu.content.model.Material;
import com.genedu.content.repository.ChapterRepository;
import com.genedu.content.service.ChapterService;
import com.genedu.content.service.MaterialService;
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
    private final MaterialService materialService;

    @Transactional(readOnly = true)
    @Override
    public List<FlatMaterialChapterDTO> getAllChapters() {
        return chapterRepository.findAll().stream()
                .map(ChapterMapper::toFlatDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChapterResponseDTO> getChaptersByMaterialId(Long materialId) {
        log.info("Fetching chapters for material with ID: {}", materialId);
        List<Chapter> chapters = chapterRepository.findByMaterial_Id(materialId);
        return chapters.stream()
                .map(ChapterMapper::toDTO)
                .toList();
    }

    @Override
    public MaterialResponseDTO getMaterialWithChapters(Long materialId) {
        var chapters = chapterRepository.findByMaterial_Id(materialId);
        var chapterResponseDTOS = chapters.stream()
                .map(ChapterMapper::toDTO)
                .toList();

        Material materialResponseDTO = materialService.getMaterialEntityById(materialId);

        return MaterialMapper.toDTOWithChapters(materialResponseDTO, chapterResponseDTOS);
    }

    @Transactional(readOnly = true)
    @Override
    public FlatMaterialChapterDTO getChapterById(Long id) {
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
    public ChapterResponseDTO getChapterByMaterialIdAndOrderNumber(Long materialId, int orderNumber) {
        Chapter chapter = getChapterEntityByMaterialIdAndOrderNumber(materialId, orderNumber);
        return ChapterMapper.toDTO(chapter);
    }

    @Transactional(readOnly = true)
    @Override
    public Chapter getChapterEntityByMaterialIdAndOrderNumber(Long materialId, int orderNumber) {
        if (materialId == null) {
            throw new BadRequestException(Constants.ErrorCode.MATERIAL_ID_REQUIRED);
        }
        if (orderNumber <= 0) {
            throw new BadRequestException(Constants.ErrorCode.CHAPTER_ORDER_NUMBER_INVALID, orderNumber);
        }
        return chapterRepository.findByMaterialIdAndOrderNumber(materialId, orderNumber)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CHAPTER_NOT_FOUND, materialId, orderNumber));
    }

    @Override
    public FlatMaterialChapterDTO createChapter(Long materialId, ChapterRequestDTO chapterRequestDTO) {
        if (chapterRepository.existsByOrderNumberAndMaterialId(chapterRequestDTO.orderNumber(), materialId)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_CHAPTER_ORDER, chapterRequestDTO.orderNumber());
        }
        Material material = materialService.getMaterialEntityById(materialId);
        Chapter chapter = ChapterMapper.toEntity(chapterRequestDTO, material);

        try {
            Chapter savedChapter = chapterRepository.save(chapter);
            return ChapterMapper.toFlatDTO(savedChapter);
        } catch (Exception e) {
            log.error("Error creating chapter", e);
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_CHAPTER_FAILED, e.getMessage());
        }
    }

    @Override
    public FlatMaterialChapterDTO updateChapter(Long id, ChapterRequestDTO chapterRequestDTO) {
        Chapter existingChapter = getChapterEntityById(id);

        if (chapterRepository.existsByOrderNumberAndMaterial_IdAndIdNot(
                chapterRequestDTO.orderNumber(),
                existingChapter.getMaterial().getId(),
                id)
        ) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_CHAPTER_ORDER, chapterRequestDTO.orderNumber());
        }



        try {
            existingChapter.setOrderNumber(chapterRequestDTO.orderNumber());
            existingChapter.setTitle(chapterRequestDTO.title());
            existingChapter.setDescription(chapterRequestDTO.description());

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
            var existingChapter = getChapterEntityById(id);
            existingChapter.setDeleted(true);
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