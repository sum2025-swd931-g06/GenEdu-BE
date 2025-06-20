package com.genedu.lecturecontent.service;

import com.genedu.lecturecontent.dto.LectureContentRequestDTO;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.List;

@Service
public class LectureContentService {

    private final VectorStore vectorStore;

    public LectureContentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void embedLectureContentToVectorStore(List<LectureContentRequestDTO> lectureContentRequestDTOS) {
        TextSplitter textSplitter = TokenTextSplitter.builder()
                .withChunkSize(512)
                .withKeepSeparator(true)
                .withMaxNumChunks(1000)
                .build();

        List<Document> documents = new ArrayList<>(lectureContentRequestDTOS.size());
        for (LectureContentRequestDTO lectureContent : lectureContentRequestDTOS) {

            String content = lectureContent.getContent();
            if (content == null || content.isEmpty()) {
                continue; // Skip empty content
            }
                // Create a Document object for each chunk
                Document document = new Document(content);
                document.getMetadata().put("schoolClassId", lectureContent.getSchoolClassId());
                document.getMetadata().put("subjectId", lectureContent.getSubjectId());
                document.getMetadata().put("materialId", lectureContent.getMaterialId());
                document.getMetadata().put("lessonId", lectureContent.getLessonId());
                document.getMetadata().put("lessonPartId", lectureContent.getLessonPartId());
                // Store the document in the vector store
                documents.add(document);
        }
        if (!documents.isEmpty()) {
            // Split the documents into smaller chunks
            List<Document> splitDocuments = textSplitter.split(documents);
            // Store the split documents in the vector store
            vectorStore.accept(splitDocuments);
        }
    }
}
