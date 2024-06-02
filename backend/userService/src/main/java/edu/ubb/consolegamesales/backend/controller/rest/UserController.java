package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.mapper.UserMapper;
import edu.ubb.consolegamesales.backend.dto.outgoing.UserAddressDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.UserResponseDto;
import edu.ubb.consolegamesales.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/users")
@AllArgsConstructor
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable("id") Long id) throws NotFoundException {
        LOGGER.info("GET user at users/{} api", id);
        try {
            UserResponseDto userResponse = userMapper.modelToResponseDto(
                    userService.loadUserByUserId(id)
            );
            if (userResponse == null) {
                throw new NotFoundException("User with ID " + id + " not found");
            } else {
                return userResponse;
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with ID " + id + " not found", e);
        }
    }


    @GetMapping("/{id}/address")
    public UserAddressDto findAddressByUserId(@PathVariable("id") Long id) throws NotFoundException {
        LOGGER.info("GET user address at users/{}/address api", id);
        try {
            String address = userService.loadUserAddressByUserId(id);
            if (address == null) {
                throw new NotFoundException("User with ID " + id + " not found");
            } else {
                return new UserAddressDto(address);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with ID " + id + " not found", e);
        }
    }
}
