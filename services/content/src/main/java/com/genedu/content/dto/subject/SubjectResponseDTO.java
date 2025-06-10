package com.genedu.content.dto.subject;

import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.model.Subject;
import lombok.Builder;

import java.util.List;

@Builder
public record SubjectResponseDTO(
        Long id,
        String name,
        String description,
        List<ChapterResponseDTO> chapters
) {}