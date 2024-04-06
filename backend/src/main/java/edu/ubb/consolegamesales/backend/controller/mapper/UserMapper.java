package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.controller.dto.outgoing.UserResponseDto;
import edu.ubb.consolegamesales.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", source = "entityId")
    UserResponseDto modelToResponseDto(User user);

    @Mapping(target = "userId", source = "entityId")
    Collection<UserResponseDto> modelsToResponseDtos(Collection<User> users);

}
