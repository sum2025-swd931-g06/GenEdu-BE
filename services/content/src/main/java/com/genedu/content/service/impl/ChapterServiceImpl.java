package com.genedu.content.service.impl;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.mapper.ChapterMapper;
import com.genedu.content.mapper.SubjectMapper;
import com.genedu.content.model.Chapter;
import com.genedu.content.model.Subject;
import com.genedu.content.repository.ChapterRepository;
import com.genedu.content.service.ChapterService;
import com.genedu.content.service.LessonService;
import com.genedu.content.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final SubjectService subjectService;

    @Override
    public List<FlatSubjectChapterDTO> getAllChapters() {
        return chapterRepository.findAll().stream()
                .map(ChapterMapper::toFlatDTO)
                .toList();
    }

    @Override
    public SubjectResponseDTO getChaptersBySubjectId(Long subjectId) {
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        List<ChapterResponseDTO> chapters = chapterRepository.findBySubject_Id(subjectId).stream()
                .map(ChapterMapper::toDTO)
                .toList();

        return SubjectMapper.toDTOWithChapters(subject, chapters);
    }

    @Override
    public FlatSubjectChapterDTO getChapterById(Long id) {
        Chapter chapter = getChapterEntityById(id);
        return ChapterMapper.toFlatDTO(chapter);
    }

    @Override
    public Chapter getChapterEntityById(Long id) {
    if (id == null) {
            throw new IllegalArgumentException("Chapter ID cannot be null");
        }
        return chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chapter with ID " + id + " not found"));
    }

    @Override
    public ChapterResponseDTO getChapterBySubjectIdAndOrderNumber(Long subjectId, int orderNumber) {

        Chapter chapter = getChapterEntityBySubjectIdAndOrderNumber(subjectId, orderNumber);
        return ChapterMapper.toDTO(chapter);
    }

    @Override
    public Chapter getChapterEntityBySubjectIdAndOrderNumber(Long subjectId, int orderNumber) {
        if(subjectId == null) {
            throw new IllegalArgumentException("Subject ID cannot be null");
        }
        if(orderNumber <= 0) {
            throw new IllegalArgumentException("Order number cannot be negative");
        }
        return chapterRepository.findBySubject_IdAndOrderNumber(subjectId, orderNumber)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Chapter with subject ID " + subjectId + " and order number " + orderNumber + " not found"));
    }

    @Override
    public FlatSubjectChapterDTO createChapter(Long subjectId, ChapterRequestDTO chapterRequestDTO) {
        if(chapterRepository.existsByOrOrderNumberAndSubject_Id(chapterRequestDTO.orderNumber(), subjectId)){
            throw new IllegalArgumentException("Chapter with order number " + chapterRequestDTO.orderNumber() + " already exists for subject ID " + subjectId);
        }
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        Chapter chapter = ChapterMapper.toEntity(chapterRequestDTO, subject);

        Chapter savedChapter = chapterRepository.save(chapter);
        return ChapterMapper.toFlatDTO(savedChapter);
    }

    @Override
    public FlatSubjectChapterDTO updateChapter(Long id, ChapterRequestDTO chapterRequestDTO) {
        Chapter existingChapter = getChapterEntityById(id);

        if(chapterRepository.existsByOrderNumberAndSubject_IdAndIdNot(chapterRequestDTO.orderNumber(), existingChapter.getSubject().getId(), id)){
            throw new IllegalArgumentException("Chapter with order number " + chapterRequestDTO.orderNumber() + " already exists for subject ID " + existingChapter.getSubject().getId());
        }

        existingChapter.setOrderNumber(chapterRequestDTO.orderNumber());
        existingChapter.setTitle(chapterRequestDTO.title());
        existingChapter.setDescription(chapterRequestDTO.description());

        Chapter updatedChapter = chapterRepository.save(existingChapter);
        return ChapterMapper.toFlatDTO(updatedChapter);
    }

    @Override
    public void deleteChapter(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Chapter ID cannot be null");
        }

        if (!chapterRepository.existsById(id)) {
            throw new IllegalArgumentException("Chapter with ID " + id + " not found");
        }

        chapterRepository.deleteById(id);
    }
}
