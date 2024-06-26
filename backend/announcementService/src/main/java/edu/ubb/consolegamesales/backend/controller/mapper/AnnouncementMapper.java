package edu.ubb.consolegamesales.backend.controller.mapper;


import edu.ubb.consolegamesales.backend.dto.incoming.AnnouncementCreationDto;
import edu.ubb.consolegamesales.backend.dto.incoming.AnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.AnnouncementDetailedDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.AnnouncementListShortDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.model.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {
    @Mapping(target = "announcementId", source = "entityId")
    @Mapping(target = "savedByUser", expression = "java(false)")
    AnnouncementDetailedDto modelToDetailedDto(Announcement announcement);


    CreatedObjectDto modelToCreatedObjDto(Announcement announcement);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "sold", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "soldGameDisc", ignore = true)
    Announcement creationDtoToModel(AnnouncementCreationDto announcementCreationDto);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "sellerId", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "soldGameDisc", ignore = true)
    Announcement updateDtoToModel(AnnouncementUpdateDto announcementUpdateDto);


    @Mapping(target = "soldGameDiscName", source = "soldGameDisc.name")
    @Mapping(target = "soldGameDiscType", source = "soldGameDisc.type")
    @Mapping(target = "announcementId", source = "entityId")
    @Mapping(target = "savedByUser", expression = "java(false)")
    AnnouncementListShortDto modelToListShortDto(Announcement announcement);

    @Mapping(target = "soldGameDiscName", source = "soldGameDisc.name")
    @Mapping(target = "announcementId", source = "entityId")
    @Mapping(target = "savedByUser", expression = "java(false)")
    List<AnnouncementListShortDto> modelsToListShortDto(List<Announcement> announcements);
}
