package com.genedu.content.controller;

import com.genedu.content.dto.schoolclass.SchoolClassRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.service.SchoolClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/school-classes")
@Tag(name = "School Class", description = "Quản lý danh sách lớp học")
public class SchoolClassController {
    private final SchoolClassService schoolClassService;

//    @GetMapping()
//    public List<SchoolClassResponseDTO> getAllSchoolClasses(@RequestParam(defaultValue = "0") int pageNo) {
//        log.info("Fetching all school classes, pageNo: {}", pageNo);
//        return schoolClassService.getAllSchoolClasses(pageNo);
//    }

    @Operation(summary = "Lấy tất cả lớp học", description = "Trả về danh sách tất cả lớp học trong hệ thống.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    @GetMapping()
    public ResponseEntity<List<SchoolClassResponseDTO>> getAllSchoolClasses() {
        log.info("Fetching all school classes");
        List<SchoolClassResponseDTO> schoolClasses = schoolClassService.getAllSchoolClasses();
        return ResponseEntity.ok(schoolClasses);
    }

    @Operation(summary = "Tạo mới lớp học", description = "Tạo một lớp học mới với tên và mô tả.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo lớp học thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    public ResponseEntity<SchoolClassResponseDTO> createSchoolClass(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin lớp học cần tạo", required = true
            )
            @RequestBody SchoolClassRequestDTO schoolClassRequestDTO
    ) {
        log.info("Creating new school class: {}", schoolClassRequestDTO);
        SchoolClassResponseDTO created = schoolClassService.createSchoolClass(schoolClassRequestDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/school-classes/" + created.id()))
                .body(created);
    }

    @Operation(summary = "Cập nhật lớp học", description = "Cập nhật thông tin của một lớp học theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật lớp học thành công"),
            @ApiResponse(responseCode = "404", description = "Lớp học không tìm thấy", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SchoolClassResponseDTO> updateSchoolClass(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID của lớp học cần cập nhật", required = true
            )
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin lớp học cần cập nhật", required = true
            )
            @RequestBody SchoolClassRequestDTO schoolClassRequestDTO
    ) {
        log.info("Updating school class with ID: {}, data: {}", id, schoolClassRequestDTO);
        SchoolClassResponseDTO updated = schoolClassService.updateSchoolClass(id, schoolClassRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Xóa lớp học", description = "Xóa một lớp học theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Xóa lớp học thành công"),
            @ApiResponse(responseCode = "404", description = "Lớp học không tìm thấy", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchoolClass(@PathVariable Integer id) {
        log.info("Deleting school class with ID: {}", id);
        schoolClassService.deleteSchoolClass(id);
        return ResponseEntity.noContent().build();
    }
}
