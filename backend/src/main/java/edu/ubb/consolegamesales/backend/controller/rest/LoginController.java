package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.LoginInformationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.TokenDto;
import edu.ubb.consolegamesales.backend.controller.mapper.UserMapper;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/login")
public class LoginController {
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;


    @PostMapping
    public TokenDto login(@RequestBody @Valid LoginInformationDto loginInformationDto, HttpServletResponse response) {
        LOGGER.info("Login request");
        User loginUser = userMapper.loginDtoToModel(loginInformationDto);
        // first is the accesToken, second the refreshToken
        Pair<String, String> tokens = authenticationService.authenticate(loginUser);
        // refreshToken is used in cookie
        Cookie authCookie = new Cookie("Auth", tokens.getSecond());
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        // max age to 5 hours
        authCookie.setMaxAge(5 * 60 * 60);
        authCookie.setAttribute("SameSite", "None");
        response.addCookie(authCookie);
        LOGGER.info("User with username '" + loginUser.getUsername() + "' logged in");
        // accesToken sent to the client application
        return new TokenDto(tokens.getFirst());
    }
}
