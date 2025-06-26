package com.genedu.lecturecontent.service;

import com.genedu.lecturecontent.dto.LectureContentRequestDTO;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<Document> documents = new ArrayList<>();

        for (LectureContentRequestDTO dto : lectureContentRequestDTOs) {
            List<String> chunks = splitTextBySentence(dto.content(), 2);

            for (int i = 0; i < chunks.size(); i++) {
                Document doc = getDocument(dto, chunks, i);
                documents.add(doc);
            }
        }

        // Add all chunks to vector store
        if (!documents.isEmpty()) {
            vectorStore.add(documents);
        }

        return lectureContentRequestDTOs;
    }

    private Document getDocument(LectureContentRequestDTO dto, List<String> chunks, int i) {
        String chunk = chunks.get(i);
        return new Document(
                chunk,
                Map.of(
                        "schoolClassId", dto.schoolClassId(),
                        "subjectId", dto.subjectId(),
                        "materialId", dto.materialId(),
                        "chapterId", dto.chapterId(),
                        "lessonId", dto.lessonId(),
                        "lessonContentId", dto.lessonContentId(),
                        "chunkIndex", String.valueOf(i)
                )
        );
    }

    private List<String> splitTextBySentence(String content, int maxSentencesPerChunk) {
        String[] sentences = content.split("(?<=[.!?])\\s+");
        List<String> chunks = new ArrayList<>();
        StringBuilder chunkBuilder = new StringBuilder();

        int sentenceCount = 0;
        for (String sentence : sentences) {
            chunkBuilder.append(sentence.trim()).append(" ");
            sentenceCount++;

            if (sentenceCount >= maxSentencesPerChunk) {
                chunks.add(chunkBuilder.toString().trim());
                chunkBuilder.setLength(0);
                sentenceCount = 0;
            }
        }

        if (!chunkBuilder.isEmpty()) {
            chunks.add(chunkBuilder.toString().trim());
        }

        return chunks;
    }
}
