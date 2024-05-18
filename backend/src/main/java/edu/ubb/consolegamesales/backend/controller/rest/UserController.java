package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.dto.outgoing.Pagination;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.UserResponseDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.UsersResponseWithPaginationDto;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.mapper.UserMapper;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/users")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public UsersResponseWithPaginationDto getUsersChatedWith(
            @RequestParam @Positive Long chattedWithUser,
            @RequestParam(defaultValue = "1", required = false) @Positive int page,
            @RequestParam(defaultValue = "5", required = false) @Positive int limit) {
        LOGGER.info("GET request at users api, get users that has messages with user with id {}", chattedWithUser);
        // get users list based if there is a message between them
        PageRequest pageRequest =
                PageRequest.of(page - 1, limit);
        Page<User> usersChattedWith =
                userRepository.findUsersChattedWith(chattedWithUser, pageRequest);
        List<UserResponseDto> userResponseDtos = userMapper.modelsToResponseDtos(usersChattedWith.getContent());
        Pagination pagination = new Pagination(page, limit,
                usersChattedWith.getTotalElements(), usersChattedWith.getTotalPages());
        return new UsersResponseWithPaginationDto(userResponseDtos, pagination);
    }

    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable("id") Long id) throws NotFoundException {
        LOGGER.info("GET user at users/{} api", id);
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
