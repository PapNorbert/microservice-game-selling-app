package edu.ubb.consolegamesales.backend.controller;

import edu.ubb.consolegamesales.backend.controller.dto.outgoing.TokenDto;
import edu.ubb.consolegamesales.backend.service.AuthenticationService;
import edu.ubb.consolegamesales.backend.util.TokenExtraction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/refresh")
public class RefreshController {
    private final AuthenticationService authenticationService;

    @GetMapping
    public TokenDto refresh(@NotNull HttpServletRequest request) {
        String token = TokenExtraction.extractTokenFromRequestCookie(request);
        return new TokenDto(authenticationService.refresh(token));
    }
}
