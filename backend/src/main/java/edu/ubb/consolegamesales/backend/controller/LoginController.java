package edu.ubb.consolegamesales.backend.controller;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.LoginInformationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.TokenDto;
import edu.ubb.consolegamesales.backend.controller.mapper.UserMapper;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@ResponseStatus(HttpStatus.CREATED)
@RequestMapping("${apiPrefix}/login")
public class LoginController {
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;


    @PostMapping
    public TokenDto login(@RequestBody @Valid LoginInformationDto loginInformationDto) {
        LOGGER.info("Login request");
        User loginUser = userMapper.LoginDtoToModel(loginInformationDto);
        return new TokenDto(authenticationService.authenticate(loginUser));
    }
}
