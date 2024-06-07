package edu.ubb.consolegamesales.backend.controller.mapper;


import edu.ubb.consolegamesales.backend.dto.incoming.AnnouncementsSavesCreationDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.model.AnnouncementsSaves;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnouncementsSavesMapper {
    CreatedObjectDto modelToCreatedObjDto(AnnouncementsSaves announcementsSaves);


    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "announcement.entityId", source = "announcementId")
    AnnouncementsSaves creationDtoToModel(AnnouncementsSavesCreationDto announcementsSavesCreationDto);
}
