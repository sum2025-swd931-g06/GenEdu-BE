package com.genedu.user.controller;

import com.genedu.user.dto.customer.CreateUserValidatorGroup;
import com.genedu.user.dto.customer.UserCreationRequestDTO;
import com.genedu.user.model.User;
import com.genedu.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "keycloak")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(
        @RequestBody @Validated({Default.class, CreateUserValidatorGroup.class}) UserCreationRequestDTO userCreationRequestDTO
    ) {
        return ResponseEntity.ok(userService.createUser(userCreationRequestDTO));
    }

    @PutMapping("{userId}")
    public ResponseEntity<Void> updateUser(
        @PathVariable UUID userId,
        @RequestBody @Validated({Default.class}) UserCreationRequestDTO userCreationRequestDTO
    ) {
        userService.updateUser(userCreationRequestDTO, userId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers () {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
