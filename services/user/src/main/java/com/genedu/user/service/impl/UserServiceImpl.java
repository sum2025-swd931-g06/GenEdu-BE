package com.genedu.user.service.impl;

import com.genedu.commonlibrary.exception.AccessDeniedException;
import com.genedu.commonlibrary.exception.ForbiddenException;
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
    private static final String ERROR_FORMAT = "%s: Client %s don't have access right for this resource";

    private static final int USER_PER_PAGE = 10;

    @Override
    public UserResponseDTO getUserProfile(String userId) {
        try {
            return UserResponseDTO.fromUserRepresentation(keycloak.realm(keycloakPropsConfig.getRealm())
                    .users()
                    .get(userId)
                    .toRepresentation());
        } catch (ForbiddenException exception) {
            throw new AccessDeniedException(
                    String.format(ERROR_FORMAT, exception.getMessage(), keycloakPropsConfig.getClientId())
            );
        }
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
        } catch (ForbiddenException exception) {
            throw new AccessDeniedException(
                    String.format(ERROR_FORMAT, exception.getMessage(), keycloakPropsConfig.getClientId())
            );
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
        } catch (ForbiddenException exception) {
            throw new AccessDeniedException(
                    String.format(ERROR_FORMAT, exception.getMessage(), keycloakPropsConfig.getClientId())
            );
        }
    }
}
