package com.genedu.user.controller;

import com.genedu.user.dto.customer.UserListResponseDTO;
import com.genedu.user.dto.customer.UserResponseDTO;
import com.genedu.user.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/users")
    ResponseEntity<UserListResponseDTO> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false)
            int pageNo
    ) {
        UserListResponseDTO users = userService.getAllUsers(pageNo);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/{userId}")
    ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable String userId) {
        UserResponseDTO userResponseDTO = userService.getUserProfile(userId);
        return ResponseEntity.ok(userResponseDTO);
    }
}
