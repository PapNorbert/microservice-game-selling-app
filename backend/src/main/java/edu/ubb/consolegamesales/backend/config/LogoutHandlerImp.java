package edu.ubb.consolegamesales.backend.config;

import edu.ubb.consolegamesales.backend.util.TokenExtraction;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class LogoutHandlerImp implements LogoutHandler {

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
    }
}
