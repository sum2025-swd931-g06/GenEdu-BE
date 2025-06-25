package com.genedu.lecturecontent.service;

import com.genedu.lecturecontent.dto.LectureContentRequestDTO;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LectureContentVectorService {
    @Qualifier("pgVectorStore")
    private final VectorStore vectorStore;

    public LectureContentVectorService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<LectureContentRequestDTO> createVectorEmbeddings(List<LectureContentRequestDTO> lectureContentRequestDTOs) {
        List<Document> documents = lectureContentRequestDTOs.stream()
                .map(dto -> new Document(
                        dto.content(),
                        Map.of(
                                "schoolClassId", dto.schoolClassId(),
                                "subjectId", dto.subjectId(),
                                "materialId", dto.materialId(),
                                "chapterId", dto.chapterId(),
                                "lessonId", dto.lessonId(),
                                "lessonContentId", dto.lessonContentId()
                        )
                ))
                .toList();
        // Store documents in the vector store
        if (!documents.isEmpty()) {
            vectorStore.add(documents);
        }
        // Retrieve embeddings from the vector store
        return lectureContentRequestDTOs.stream()
                .map(dto -> new LectureContentRequestDTO(
                        dto.schoolClassId(),
                        dto.subjectId(),
                        dto.materialId(),
                        dto.chapterId(),
                        dto.lessonId(),
                        dto.lessonContentId(),
                        dto.content()
                ))
                .toList();
    }
}
