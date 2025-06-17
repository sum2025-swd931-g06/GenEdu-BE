package com.genedu.content.service;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatMaterialChapterDTO;
import com.genedu.content.dto.material.MaterialResponseDTO;
import com.genedu.content.model.Chapter;

import java.util.List;

public interface ChapterService {
    List<FlatMaterialChapterDTO> getAllChapters();
    List<ChapterResponseDTO> getChaptersByMaterialId(Long materialId);
    MaterialResponseDTO getMaterialwithChapters(Long chapterId);
    FlatMaterialChapterDTO getChapterById(Long id);
    Chapter getChapterEntityById(Long id);

    ChapterResponseDTO getChapterBySubjectIdAndOrderNumber(Long materialId, int orderNumber);
    Chapter getChapterEntityBySubjectIdAndOrderNumber(Long materialId, int orderNumber);
    FlatMaterialChapterDTO createChapter(Long materialId, ChapterRequestDTO chapterRequestDTO);
    FlatMaterialChapterDTO updateChapter(Long id, ChapterRequestDTO chapter);
    void deleteChapter(Long id);
}
