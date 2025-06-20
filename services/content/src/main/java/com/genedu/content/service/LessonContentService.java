package com.genedu.content.service;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatLessonLessonContentDTO;
import com.genedu.content.dto.flatResponse.FlatMaterialChapterDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.dto.lessoncontent.LessonContentRequestDTO;
import com.genedu.content.dto.lessoncontent.LessonContentResponseDTO;
import com.genedu.content.dto.material.MaterialResponseDTO;
import com.genedu.content.model.Chapter;
import com.genedu.content.model.LessonContent;

import java.util.List;

public interface LessonContentService {

    List<FlatLessonLessonContentDTO> getAllLessonContents();
    LessonContent getLessonContentEntityById(Long id);
    FlatLessonLessonContentDTO getLessonContentById(Long id);
    LessonResponseDTO getLessonContentsByLessonId(Long lessonId);
    LessonResponseDTO getLessonWithLessonContents(Long lessonId);



    LessonContentResponseDTO getLessonContentByLessonIdAndOrderNumber(Long lessonId, int orderNumber);
    LessonContent getLessonContentEntityByLessonIdAndOrderNumber(Long lessonId, int orderNumber);
    FlatLessonLessonContentDTO createLessonContent(Long lessonId, LessonContentRequestDTO lessonContentRequestDTO);
    FlatLessonLessonContentDTO updateLessonContent(Long id, LessonContentRequestDTO lessonContentRequestDTO);
    void deleteLessonContent(Long id);}
