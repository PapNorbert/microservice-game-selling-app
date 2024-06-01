package edu.ubb.consolegamesales.backend.filter;

import edu.ubb.consolegamesales.backend.service.JwtService;
import edu.ubb.consolegamesales.backend.service.RedisService;
import edu.ubb.consolegamesales.backend.util.TokenExtraction;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    private final RedisService redisService;
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {


        // extract the jwt token from cookies
        String token = TokenExtraction.extractTokenFromRequestCookie(request);
        if (token == null) {
            // user is not logged in
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = jwtService.decodeUserId(token);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (userId != null && securityContext.getAuthentication() == null) {
            // user not already authenticated
            // get user data
            UserDetails userDetails = redisService.getCachedUser(userId);

            if (userDetails != null && jwtService.verifyToken(token, userDetails)) {
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
