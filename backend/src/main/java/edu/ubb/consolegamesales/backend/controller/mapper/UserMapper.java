package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.controller.dto.outgoing.UserResponseDto;
import edu.ubb.consolegamesales.backend.model.User;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto modelToResponseDto(User user);
    Collection<UserResponseDto> modelsToResponseDtos(Collection<User> users);

}
