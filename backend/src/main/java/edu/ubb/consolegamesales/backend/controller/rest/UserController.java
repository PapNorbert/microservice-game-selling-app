package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.dto.outgoing.UserResponseDto;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.mapper.UserMapper;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable("id") Long id) throws NotFoundException {
        LOGGER.info("GET user at users/" + id  + "api");
        try {
            UserResponseDto userResponse = userMapper.modelToResponseDto(userRepository.getById(id));
            if (userResponse == null) {
                throw new NotFoundException("User with ID " + id + " not found");
            } else {
                return userResponse;
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with ID " + id + " not found", e);
        }
    }

}
