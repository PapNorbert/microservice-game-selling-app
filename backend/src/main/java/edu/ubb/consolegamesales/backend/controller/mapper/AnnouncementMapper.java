package edu.ubb.consolegamesales.backend.controller.mapper;


import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.AnnouncementDetailedDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.model.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {
    @Mapping(target = "announcementId", source = "entityId")
    @Mapping(target = "sellerId", source = "seller.entityId")
    AnnouncementDetailedDto modelToDetailedDto(Announcement announcement);

    @Mapping(target = "announcementId", source = "entityId")
    Collection<AnnouncementDetailedDto> modelsToResponseDtos(Collection<Announcement> announcements);

    CreatedObjectDto modelToCreatedObjDto(Announcement announcement);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "seller.entityId", source = "sellerId")
    @Mapping(target = "soldGameDisc.entityId", source = "soldGameDiscId")
    Announcement creationDtoToModel(AnnouncementCreationDto announcementCreationDto);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "soldGameDisc.entityId", source = "soldGameDiscId")
    Announcement updateDtoToModel(AnnouncementUpdateDto announcementUpdateDto);
}
