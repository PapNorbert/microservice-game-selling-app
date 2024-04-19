package edu.ubb.consolegamesales.backend.controller;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.UserCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
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
@RequestMapping("${apiPrefix}/register")
public class RegistrationController {
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;


    @PostMapping
    public CreatedObjectDto register(@RequestBody @Valid UserCreationDto userCreationDto) {
        LOGGER.info("Registration request");
        User createdUser = authenticationService.register(userMapper.creationDtoToModel(userCreationDto));
        return userMapper.modelToCreatedObjDto(createdUser);
    }

}
