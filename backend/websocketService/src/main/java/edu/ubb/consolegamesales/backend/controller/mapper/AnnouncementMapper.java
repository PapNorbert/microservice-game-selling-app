package edu.ubb.consolegamesales.backend.controller.mapper;


import edu.ubb.consolegamesales.backend.dto.outgoing.AnnouncementListShortDto;
import edu.ubb.consolegamesales.backend.model.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {
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
