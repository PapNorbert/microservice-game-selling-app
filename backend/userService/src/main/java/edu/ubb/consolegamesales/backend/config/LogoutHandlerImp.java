package edu.ubb.consolegamesales.backend.config;

import edu.ubb.consolegamesales.backend.service.JwtService;
import edu.ubb.consolegamesales.backend.util.TokenExtraction;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class LogoutHandlerImp implements LogoutHandler {
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = TokenExtraction.extractTokenFromRequestCookie(request);
        if (token == null) {
            return;
        }
        // remove cookie by setting maxAge to 0 overwriting the existing cookie
        Cookie authCookie = new Cookie("Auth", "");
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge(0);
        authCookie.setAttribute("SameSite", "None");
        response.addCookie(authCookie);
        String username = jwtService.decodeUsername(token);
        LOGGER.info("User with username '{}' logged out", username);
    }
}
