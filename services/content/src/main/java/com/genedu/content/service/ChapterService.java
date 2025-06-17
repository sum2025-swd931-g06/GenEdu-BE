package com.genedu.content.service;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.Chapter;

import java.util.List;

public interface ChapterService {
    List<FlatSubjectChapterDTO> getAllChapters();
    SubjectResponseDTO getChaptersBySubjectId(Long subjectId);
    FlatSubjectChapterDTO getChapterById(Long id);
    Chapter getChapterEntityById(Long id);
//    ChapterResponseDTO getChapterLessonsById(Long id);
    ChapterResponseDTO getChapterBySubjectIdAndOrderNumber(Long subjectId, int orderNumber);
    Chapter getChapterEntityBySubjectIdAndOrderNumber(Long subjectId, int orderNumber);
    FlatSubjectChapterDTO createChapter(Long subjectId, ChapterRequestDTO chapterRequestDTO);
    FlatSubjectChapterDTO updateChapter(Long id, ChapterRequestDTO chapter);
    void deleteChapter(Long id);
}
