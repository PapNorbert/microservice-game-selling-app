package edu.ubb.consolegamesales.backend.controller.mapper;


import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.controller.dto.incoming.GameDiscCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.AnnouncementDetailedDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.AnnouncementListShortDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.GameDisc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {
    @Mapping(target = "announcementId", source = "entityId")
    @Mapping(target = "sellerId", source = "seller.entityId")
    AnnouncementDetailedDto modelToDetailedDto(Announcement announcement);

    @Mapping(target = "announcementId", source = "entityId")
    Collection<AnnouncementDetailedDto> modelsToResponseDtos(Collection<Announcement> announcements);

    CreatedObjectDto modelToCreatedObjDto(Announcement announcement);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "sold", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "soldGameDisc", ignore = true)
    @Mapping(target = "seller.entityId", source = "sellerId")
    Announcement creationDtoToModel(AnnouncementCreationDto announcementCreationDto);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "soldGameDisc.entityId", source = "soldGameDiscId")
    Announcement updateDtoToModel(AnnouncementUpdateDto announcementUpdateDto);


    @Mapping(target = "soldGameDiscName", source = "soldGameDisc.name")
    @Mapping(target = "announcementId", source = "entityId")
    AnnouncementListShortDto modelToListShortDto(Announcement announcement);

    @Mapping(target = "soldGameDiscName", source = "soldGameDisc.name")
    @Mapping(target = "announcementId", source = "entityId")
    List<AnnouncementListShortDto> modelsToListShortDto(List<Announcement> announcements);
}
