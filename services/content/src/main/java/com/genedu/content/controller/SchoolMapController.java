package com.genedu.content.controller;

import com.genedu.content.dto.flatResponse.SchoolDataMapResponse;
import com.genedu.content.service.SchoolMapService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents")
@Tag(name = "School Map API", description = "Provides flat map data of school classes, subjects, materials, and lessons for frontend consumption.")
public class SchoolMapController {
    private final SchoolMapService schoolMapService;

    @GetMapping("/school-map")
    public ResponseEntity<SchoolDataMapResponse> getFlatSchoolMap() {
        SchoolDataMapResponse schoolDataMapResponse = schoolMapService.getAllDataAsMap();
        return ResponseEntity.ok(schoolDataMapResponse);
        //        return schoolMapService.getAllDataAsMap();
    }
}
