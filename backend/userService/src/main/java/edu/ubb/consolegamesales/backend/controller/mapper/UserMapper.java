package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.dto.incoming.LoginInformationDto;
import edu.ubb.consolegamesales.backend.dto.incoming.UserCreationDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.UserResponseDto;
import edu.ubb.consolegamesales.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", source = "entityId")
    UserResponseDto modelToResponseDto(User user);

    @Mapping(target = "userId", source = "entityId")
    List<UserResponseDto> modelsToResponseDtos(List<User> users);

    CreatedObjectDto modelToCreatedObjDto(User user);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User creationDtoToModel(UserCreationDto userCreationDto);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User loginDtoToModel(LoginInformationDto loginInformationDto);

}
