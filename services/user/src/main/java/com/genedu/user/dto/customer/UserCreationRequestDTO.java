package com.genedu.user.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserCreationRequestDTO(
        @NotNull(message = "User first name is required")
        String firstName,
        @NotNull(message = "User last name is required")
        String lastName,
        @NotNull(message = "User email is required")
        @Email(message = "User email must be a valid email address")
        String email
) {
}
