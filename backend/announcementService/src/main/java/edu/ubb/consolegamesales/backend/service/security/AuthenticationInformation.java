package edu.ubb.consolegamesales.backend.service.security;

import edu.ubb.consolegamesales.backend.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

public class AuthenticationInformation {

    public static User extractUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null) {
            throw new AccessDeniedException("You are not logged in!");
        }
        return (User) authentication.getPrincipal();
    }
}
