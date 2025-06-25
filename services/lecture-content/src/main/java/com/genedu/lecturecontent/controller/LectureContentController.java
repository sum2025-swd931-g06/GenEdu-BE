package com.genedu.lecturecontent.controller;

import com.genedu.lecturecontent.dto.LectureContentRequestDTO;
import com.genedu.lecturecontent.service.LectureContentVectorService;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/lecture-contents")
public class LectureContentController {
    private static final Logger log = Logger.getLogger(LectureContentController.class.getName());
    private final LectureContentVectorService lectureContentVectorService;

    public LectureContentController(LectureContentVectorService lectureContentVectorService) {
        this.lectureContentVectorService = lectureContentVectorService;
    }

    @PostMapping(
        value = "/embeddings",
        produces = "application/json" + ";charset=UTF-8",
        consumes = "application/json" + ";charset=UTF-8"
    )
    public List<LectureContentRequestDTO> createVectorEmbeddings(
            @RequestBody List<LectureContentRequestDTO> lectureContentRequestDTOs
    ) {
        log.info("Creating vector embeddings for lecture content...");
        List<LectureContentRequestDTO> embeddings = lectureContentVectorService.createVectorEmbeddings(lectureContentRequestDTOs);
        if (embeddings.isEmpty()) {
            log.warning("No embeddings created. The input list was empty.");
        }
        return embeddings;
    }
}
