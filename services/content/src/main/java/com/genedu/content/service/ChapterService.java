package com.genedu.content.service;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterDTO;
import com.genedu.content.model.Chapter;

import java.util.List;

public interface ChapterService {
    List<FlatSubjectChapterDTO> getAllChapters();
    List<ChapterResponseDTO> getChaptersBySubjectId(Long subjectId);
    FlatSubjectChapterDTO getChapterById(Long id);
    Chapter getChapterEntityById(Long id);
//    ChapterResponseDTO getChapterLessonsById(Long id);
    ChapterResponseDTO getChapterBySubjectIdAndOrderNumber(Long subjectId, int orderNumber);
    Chapter getChapterEntityBySubjectIdAndOrderNumber(Long subjectId, int orderNumber);
    ChapterResponseDTO createChapter(Long subjectId, ChapterRequestDTO chapterRequestDTO);
    ChapterResponseDTO updateChapter(Long id, ChapterRequestDTO chapter);
    void deleteChapter(Long id);
}
