package com.genedu.user.controller;

import com.genedu.user.dto.customer.UserResponseDTO;
import com.genedu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.clearUserSession(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        log.info("Fetching user profile for user: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        UserResponseDTO  userResponseDTO = userService.getUserProfile(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(userResponseDTO);
    }

}
