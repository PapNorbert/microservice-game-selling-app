package edu.ubb.consolegamesales.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class TokenExtraction {

    public static String extractTokenFromRequestCookie(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Auth")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
}
