package com.genedu.user.service.impl;

import com.genedu.user.dto.customer.UserAdminDTO;
import com.genedu.user.dto.customer.UserListResponseDTO;
import com.genedu.user.dto.customer.UserResponseDTO;
import com.genedu.user.exception.UserNotFoundException;
import com.genedu.user.model.User;
import com.genedu.user.service.UserService;
import com.genedu.user.configuration.KeycloakPropsConfig;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;
    private final KeycloakPropsConfig keycloakPropsConfig;

    private static final int USER_PER_PAGE = 10;

    @Override
    public UserResponseDTO getUserProfile(String userId) {
        UserRepresentation userRepresentation = null;
        try {
            userRepresentation = keycloak.realm(keycloakPropsConfig.getRealm())
                    .users()
                    .get(userId)
                    .toRepresentation();
        } catch (Exception e) {
            log.error("Error fetching user with ID {}: {}", userId, e.getMessage());
        }

        if (userRepresentation == null) {
            log.error("User with ID {} not found", userId);
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        return UserResponseDTO.fromUserRepresentation(userRepresentation);
    }

    @Override
    public UserListResponseDTO getAllUsers(int pageNo) {
        try {
            List<UserAdminDTO> result = keycloak.realm(keycloakPropsConfig.getRealm()).users()
                    .search(null, pageNo * USER_PER_PAGE, USER_PER_PAGE).stream()
                    .filter(UserRepresentation::isEnabled)
                    .map(UserAdminDTO::fromUserRepresentation)
                    .toList();
            int totalUser = result.size();

            return new UserListResponseDTO(totalUser, result, (totalUser + USER_PER_PAGE - 1) / USER_PER_PAGE);
        } catch (RuntimeException exception) {
            log.error("Error fetching users: {}", exception.getMessage());
            throw new RuntimeException("Failed to fetch users: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void clearUserSession(String userId) {
        try {
            keycloak.realm(keycloakPropsConfig.getRealm())
                    .users()
                    .get(userId)
                    .logout();
            log.info("User session cleared for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error clearing session for user ID {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to clear user session: " + e.getMessage(), e);
        }
    }
}
