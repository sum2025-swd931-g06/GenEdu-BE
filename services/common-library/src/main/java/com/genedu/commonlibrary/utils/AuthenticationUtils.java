package com.genedu.commonlibrary.utils;

import com.genedu.commonlibrary.constants.ApiConstant;
import com.genedu.commonlibrary.exception.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public final class AuthenticationUtils {

    private AuthenticationUtils() {
    }

    public static String extractUserId() {
        Authentication authentication = getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException(ApiConstant.ACCESS_DENIED);
        }

        JwtAuthenticationToken contextHolder = (JwtAuthenticationToken) authentication;

        return contextHolder.getToken().getSubject();
    }

    public static String extractJwt() {
        return ((Jwt) getAuthentication().getPrincipal()).getTokenValue();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static UUID getUserId() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return UUID.fromString(authentication.getName());
        }
        return null;
    }

}
