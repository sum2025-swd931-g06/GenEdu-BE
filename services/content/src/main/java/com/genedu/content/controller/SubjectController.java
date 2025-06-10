//package com.genedu.content.controller;
//
//import com.genedu.content.dto.subject.SubjectRequestDTO;
//import com.genedu.content.dto.subject.SubjectResponseDTO;
//import com.genedu.content.service.ChapterService;
//import com.genedu.content.service.SubjectService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/subjects")
//@Tag(name = "Subject", description = "Manage subjects in the system")
//public class SubjectController {
//    private final SubjectService subjectService;
//    private final ChapterService chapterService;
//
//    @Operation(summary = "Lấy tất cả môn học", description = "Trả về danh sách tất cả môn học trong hệ thống.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Lấy danh sách môn học thành công"),
//            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
//    })
//    @GetMapping
//    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
//        log.info("Fetching all subjects");
//        List<SubjectResponseDTO> subjects = subjectService.getAllSubjects();
//        return ResponseEntity.ok(subjects);
//    }
//
//    @Operation(summary = "Lấy môn học theo ID", description = "Trả về thông tin môn học theo ID.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Lấy môn học thành công"),
//            @ApiResponse(responseCode = "404", description = "Môn học không tìm thấy", content = @Content),
//            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<SubjectResponseDTO> getSubjectById(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "ID của môn học cần lấy thông tin", required = true
//            )
//            @PathVariable Long id
//    ) {
//        log.info("Fetching subject with ID: {}", id);
//        SubjectResponseDTO subject = subjectService.getSubjectById(id);
//        return ResponseEntity.ok(subject);
//    }
//
//    @Operation(summary = "Tạo môn học mới", description = "Tạo một môn học mới với thông tin được cung cấp.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Tạo môn học thành công"),
//            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ", content = @Content),
//            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
//    })
//    @PostMapping
//    public ResponseEntity<SubjectResponseDTO> createSubject(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Thông tin môn học cần tạo", required = true
//            )
//            @RequestBody SubjectRequestDTO subjectRequestDTO
//    ) {
//        log.info("Creating new subject: {}", subjectRequestDTO);
//        SubjectResponseDTO createdSubject = subjectService.createSubject(subjectRequestDTO);
//        return ResponseEntity
//                .created(URI.create("/api/v1/subjects/" + createdSubject.id()))
//                .body(createdSubject);
//    }
//
//    @Operation(summary = "Cập nhật môn học", description = "Cập nhật thông tin môn học theo ID.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Cập nhật môn học thành công"),
//            @ApiResponse(responseCode = "404", description = "Môn học không tìm thấy", content = @Content),
//            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
//    })
//    @PutMapping("/{id}")
//    public ResponseEntity<SubjectResponseDTO> updateSubject(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "ID của môn học cần cập nhật", required = true
//            )
//            @PathVariable Long id,
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Thông tin môn học cần cập nhật", required = true
//            )
//            @RequestBody SubjectRequestDTO subjectRequestDTO
//    ) {
//        log.info("Updating subject with ID: {}", id);
//        SubjectResponseDTO updatedSubject = subjectService.updateSubject(id, subjectRequestDTO);
//        return ResponseEntity.ok(updatedSubject);
//    }
//
//    @Operation(summary = "Xóa môn học", description = "Xóa môn học theo ID.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Xóa môn học thành công"),
//            @ApiResponse(responseCode = "404", description = "Môn học không tìm thấy", content = @Content),
//            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSubject(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "ID của môn học cần xóa", required = true
//            )
//            @PathVariable Long id
//    ) {
//        log.info("Deleting subject with ID: {}", id);
//        subjectService.deleteSubject(id);
//        return ResponseEntity.noContent().build();
//    }
//}
