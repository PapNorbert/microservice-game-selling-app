package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.MessageIncomingDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.MessageOutgoingDto;
import edu.ubb.consolegamesales.backend.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "sentTime", expression = "java(new java.util.Date())")
    @Mapping(target = "sender.entityId", source = "senderId")
    @Mapping(target = "sender.username", source = "senderUsername")
    @Mapping(target = "receiver.entityId", source = "receiverId")
    Message incomingDtoToModel(MessageIncomingDto messageIncomingDto);

    CreatedObjectDto modelToCreatedObjDto(Message message);

    @Mapping(target = "senderId", source = "sender.entityId")
    @Mapping(target = "senderUsername", source = "sender.username")
    @Mapping(target = "receiverId", source = "receiver.entityId")
    MessageOutgoingDto modelToMessageOutgoingDto(Message message);
}
