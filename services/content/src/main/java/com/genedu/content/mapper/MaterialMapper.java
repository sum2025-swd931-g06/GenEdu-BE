package com.genedu.content.mapper;

import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectMaterialDTO;
import com.genedu.content.dto.material.MaterialRequestDTO;
import com.genedu.content.dto.material.MaterialResponseDTO;
import com.genedu.content.model.Material;
import com.genedu.content.model.Subject;

import java.util.List;

public class MaterialMapper {
    public static MaterialResponseDTO toDTO(Material material){
        return MaterialResponseDTO.builder()
                .id(material.getId())
                .title(material.getTitle())
                .orderNumber(material.getOrderNumber())
                .description(material.getDescription())
                .createdOn(material.getCreatedOn())
                .createdBy(material.getCreatedBy())
                .lastModifiedOn(material.getLastModifiedOn())
                .lastModifiedBy(material.getLastModifiedBy())
                .build();
    }

    public static MaterialResponseDTO toDTOWithChapters(Material material, List<ChapterResponseDTO> chapters){
        return MaterialResponseDTO.builder()
                .id(material.getId())
                .title(material.getTitle())
                .orderNumber(material.getOrderNumber())
                .description(material.getDescription())
                .chapters(chapters)
                .createdOn(material.getCreatedOn())
                .createdBy(material.getCreatedBy())
                .lastModifiedOn(material.getLastModifiedOn())
                .lastModifiedBy(material.getLastModifiedBy())
                .build();
    }

    public static FlatSubjectMaterialDTO toFlatDTO(Material material) {
        return FlatSubjectMaterialDTO.builder()
                .subjectId(material.getSubject().getId())
                .subjectName(material.getSubject().getName())
                .subjectDescription(material.getSubject().getDescription())

                .materialId(material.getId())
                .title(material.getTitle())
                .orderNumber(material.getOrderNumber())
                .description(material.getDescription())
                .build();
    }
    public static Material toEntity(MaterialRequestDTO materialRequestDTO, Subject subject) {
        return Material.builder()
                .title(materialRequestDTO.title())
                .orderNumber(materialRequestDTO.orderNumber())
                .description(materialRequestDTO.description())
                .subject(subject)
                .build();
    }
}
