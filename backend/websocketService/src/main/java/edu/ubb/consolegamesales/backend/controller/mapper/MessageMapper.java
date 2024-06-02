package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.dto.incoming.MessageIncomingDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.MessageOutgoingDto;
import edu.ubb.consolegamesales.backend.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "sentTime", expression = "java(new java.util.Date())")
    Message incomingDtoToModel(MessageIncomingDto messageIncomingDto);

    MessageOutgoingDto modelToMessageOutgoingDto(Message message);

    List<MessageOutgoingDto> modelsToMessageOutgoingDtos(List<Message> messages);
}
