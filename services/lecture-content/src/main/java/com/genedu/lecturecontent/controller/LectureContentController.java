package com.genedu.lecturecontent.controller;

import com.genedu.lecturecontent.dto.LectureContentRequestDTO;
import com.genedu.lecturecontent.service.LectureContentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecture-contents")
public class LectureContentController {
    private final LectureContentService lectureContentService;

    public LectureContentController(LectureContentService lectureContentService) {
        this.lectureContentService = lectureContentService;
    }

    @GetMapping("/update")
    public String updateLectureContent(
            ) {
        List<LectureContentRequestDTO> lectureContentRequestDTOs = List.of(
                new LectureContentRequestDTO("class1", "subject1", "material1", "lesson1", "part1", "This is the content for class 1, subject 1, material 1, lesson 1, part 1."),
                new LectureContentRequestDTO("class2", "subject2", "material2", "lesson2", "part2", "This is the content for class 2, subject 2, material 2, lesson 2, part 2."),
                new LectureContentRequestDTO("class3", "subject3", "material3", "lesson3", "part3", "This is the content for class 3, subject 3, material 3, lesson 3, part 3.")
        );
        lectureContentService.embedLectureContentToVectorStore(lectureContentRequestDTOs);
        return "Lecture content updated successfully.";
    }
}
