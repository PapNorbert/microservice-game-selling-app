package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.GameDiscCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.GameDiscResponseDto;
import edu.ubb.consolegamesales.backend.model.GameDisc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface GameDiscMapper {
    @Mapping(target = "gameDiscId", source = "entityId")
    GameDiscResponseDto modelToResponseDto(GameDisc gameDisc);

    @Mapping(target = "gameDiscId", source = "entityId")
    Collection<GameDiscResponseDto> modelsToResponseDtos(Collection<GameDisc> gameDisc);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "sold", ignore = true)
    @Mapping(target = "user.entityId", source = "userId")
    GameDisc creationDtoToModel(GameDiscCreationDto gameDiscCreationDto);
}
