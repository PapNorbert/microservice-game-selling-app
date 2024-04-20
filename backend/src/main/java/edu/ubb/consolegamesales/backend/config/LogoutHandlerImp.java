package edu.ubb.consolegamesales.backend.config;

import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = TokenExtraction.extractTokenFromRequestCookie(request);
        if (token == null) {
            return;
        }
        // delete refresh token
        User user = userRepository.findByRefreshToken(token);
        user.setRefreshToken(null);
        userRepository.update(user.getEntityId(), user);
        // remove cookie by setting maxAge to 0 overwriting the existing cookie
        Cookie authCookie = new Cookie("Auth", "");
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge(0);
        authCookie.setAttribute("SameSite", "None");
        response.addCookie(authCookie);
        LOGGER.info("User with username '" + user.getUsername() + "' logged out");
    }
}
