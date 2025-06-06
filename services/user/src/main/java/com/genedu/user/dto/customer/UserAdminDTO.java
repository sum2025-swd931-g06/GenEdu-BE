package com.genedu.user.dto.customer;

import org.keycloak.representations.idm.UserRepresentation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public record UserAdminDTO(String id, String username, String email, String firstName, String lastName,
                              LocalDateTime createdTimestamp) {
    public static UserAdminDTO fromUserRepresentation(UserRepresentation userRepresentation) {
        LocalDateTime createdTimestamp =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(userRepresentation.getCreatedTimestamp()),
                        TimeZone.getDefault().toZoneId());
        return new UserAdminDTO(userRepresentation.getId(), userRepresentation.getUsername(),
                userRepresentation.getEmail(), userRepresentation.getFirstName(), userRepresentation.getLastName(),
                createdTimestamp);
    }
}