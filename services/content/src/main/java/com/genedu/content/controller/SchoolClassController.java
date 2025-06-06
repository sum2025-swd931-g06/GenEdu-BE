package com.genedu.content.controller;

import com.genedu.content.dto.SchoolClassResponseDTO;
import com.genedu.content.service.SchoolClassService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/school-classes")
public class SchoolClassController {
    private final SchoolClassService schoolClassService;

    @GetMapping()
    public List<SchoolClassResponseDTO> getAllSchoolClasses(@RequestParam(defaultValue = "0") int pageNo) {
        log.info("Fetching all school classes, pageNo: {}", pageNo);
        return schoolClassService.getAllSchoolClasses(pageNo);
    }
}
