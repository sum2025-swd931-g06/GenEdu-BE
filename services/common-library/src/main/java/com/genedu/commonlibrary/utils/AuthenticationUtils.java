package com.genedu.commonlibrary.utils;

import com.genedu.commonlibrary.constants.ApiConstant;
import com.genedu.commonlibrary.exception.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

@Slf4j
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
//        return SecurityContextHolder.getContext().getAuthentication();
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current authentication: " + auth);
        return auth;
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


    public static String getUserEmail() {
        return getClaim("email");
    }

    public static String getUserName() {
        var result = getClaim("name");
        return result;
    }

    private static String getClaim(String claimName) {
        Authentication authentication = getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Object claimValue = jwtAuth.getToken().getClaim(claimName);
            return claimValue != null ? claimValue.toString() : null;
        }
        return null;
    }
    
    public static UUID extracUserIdFromJwt(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            return null;
        }
        Jwt jwtToken = Jwt.withTokenValue(jwt).build();
        String userId = jwtToken.getClaimAsString("sub");
        log.info("Extracted user ID from JWT: {}", userId);
        return UUID.fromString(userId);

    }
}
