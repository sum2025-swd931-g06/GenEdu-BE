package com.genedu.content.controller;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterDTO;
import com.genedu.content.service.ChapterService;
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
@RequestMapping("/api/v1/contents")
@Tag(name = "Chapter", description = "Manage chapters in the system")
public class ChapterController {
    private final ChapterService chapterService;

    //TEST
    @Operation(summary = "Lấy tất cả chương", description = "Trả về danh sách tất cả chương trong hệ thống.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách chương thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    @GetMapping("/chapters")
    public ResponseEntity<List<FlatSubjectChapterDTO>> getAllChapters() {
        log.info("Fetching all chapters");
        return ResponseEntity.ok(chapterService.getAllChapters());
    }

    @GetMapping("/chapters/{id}")
    @Operation(summary = "Lấy chương theo ID", description = "Trả về thông tin chương theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy chương thành công"),
            @ApiResponse(responseCode = "404", description = "Chương không tìm thấy", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    public ResponseEntity<FlatSubjectChapterDTO> getChapterById(@PathVariable Long id) {
        log.info("Fetching chapter with ID: {}", id);
        FlatSubjectChapterDTO chapter = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapter);
    }

    @GetMapping("/subjects/{subjectId}/chapters")
    @Operation(summary = "Lấy tất cả chương của môn học", description = "Trả về danh sách tất cả chương của môn học theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách chương thành công"),
            @ApiResponse(responseCode = "404", description = "Môn học không tìm thấy", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    public ResponseEntity<List<ChapterResponseDTO>> getChaptersBySubjectId(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID của môn học cần lấy chương", required = true
            )
            @PathVariable Long subjectId
    ) {
        log.info("Fetching chapters for subject with ID: {}", subjectId);
        List<ChapterResponseDTO> chapters = chapterService.getChaptersBySubjectId(subjectId);
        return ResponseEntity.ok(chapters);
    }

    @PostMapping("/subjects/{subjectId}/chapters")
    @Operation(summary = "Tạo chương mới cho môn học", description = "Tạo một chương mới cho môn học theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo chương thành công"),
            @ApiResponse(responseCode = "404", description = "Môn học không tìm thấy", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    public ResponseEntity<ChapterResponseDTO> createChapter(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID của môn học cần tạo chương", required = true
            )
            @PathVariable Long subjectId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin chương cần tạo", required = true
            )
            @RequestBody ChapterRequestDTO chapterRequestDTO
    ) {
        log.info("Creating new chapter for subject with ID: {}", subjectId);
        ChapterResponseDTO createdChapter = chapterService.createChapter(subjectId, chapterRequestDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/subjects/" + subjectId + "/chapters/" + createdChapter.orderNumber()))
                .body(createdChapter);
    }

    @PutMapping("chapters/{id}")
    public ResponseEntity<ChapterResponseDTO> updateChapter(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID của chương cần cập nhật", required = true
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin chương cần cập nhật", required = true
            )
            @RequestBody ChapterRequestDTO chapterRequestDTO
    ) {
        log.info("Updating chapter with ID: {}", id);
        ChapterResponseDTO updatedChapter = chapterService.updateChapter(id, chapterRequestDTO);
        return ResponseEntity.ok(updatedChapter);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa chương", description = "Xóa chương theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Xóa chương thành công"),
            @ApiResponse(responseCode = "404", description = "Chương không tìm thấy", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content)
    })
    public ResponseEntity<Void> deleteChapter(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID của chương cần xóa", required = true
            )
            @PathVariable Long id
    ) {
        log.info("Deleting chapter with ID: {}", id);
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

}
