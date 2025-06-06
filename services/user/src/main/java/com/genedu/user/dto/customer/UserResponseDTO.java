package com.genedu.user.dto.customer;

import org.keycloak.representations.idm.UserRepresentation;

public record UserResponseDTO(
        String id, String username, String email, String firstName, String lastName
) {
    public static UserResponseDTO fromUserRepresentation(UserRepresentation userRepresentation) {
        return new UserResponseDTO(userRepresentation.getId(), userRepresentation.getUsername(),
                userRepresentation.getEmail(), userRepresentation.getFirstName(), userRepresentation.getLastName());
    }
}
