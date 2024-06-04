package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "orderDate", expression = "java(new java.util.Date())")
    Order creationDtoToModel(OrderCreationDto orderCreationDto);
}
