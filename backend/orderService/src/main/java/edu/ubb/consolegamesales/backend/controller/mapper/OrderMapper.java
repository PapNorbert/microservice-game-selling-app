package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    CreatedObjectDto modelToCreatedObjDto(Order order);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "orderDate", expression = "java(new java.util.Date())")
    Order creationDtoToModel(OrderCreationDto orderCreationDto);

//    @Mapping(target = "orderId", source = "entityId")
//    OrderListDto modelToOrderListDto(Order order);
//
//    @Mapping(target = "orderId", source = "entityId")
//    List<OrderListDto> modelsToOrderListDtos(List<Order> orders);
}
