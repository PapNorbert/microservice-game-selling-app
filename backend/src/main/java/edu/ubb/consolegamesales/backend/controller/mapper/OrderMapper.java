package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    CreatedObjectDto modelToCreatedObjDto(Order order);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "buyer.entityId", source = "buyerId")
    @Mapping(target = "announcement.entityId", source = "announcementId")
    @Mapping(target = "orderDate", expression = "java(new java.util.Date())")
    Order creationDtoToModel(OrderCreationDto orderCreationDto);
}
