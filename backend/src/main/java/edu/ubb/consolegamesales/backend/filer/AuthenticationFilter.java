package edu.ubb.consolegamesales.backend.filer;

import edu.ubb.consolegamesales.backend.service.JwtService;
import edu.ubb.consolegamesales.backend.service.security.UserDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImp userDetailsService;


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {


        // extract the jwt token from cookies
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

        if (token == null) {
            // user is not logged in
            filterChain.doFilter(request, response);
            return;
        }


        String username = jwtService.decodeUsername(token);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (username != null && securityContext.getAuthentication() == null) {
            // user not already authenticated
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // get user data from database
            if (jwtService.verifyToken(token, userDetails)) {
                // valid token, authenticate user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
