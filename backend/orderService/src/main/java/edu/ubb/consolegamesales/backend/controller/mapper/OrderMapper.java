package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    CreatedObjectDto modelToCreatedObjDto(Order order);

    // TODO mapper
//    @Mapping(target = "orderId", source = "entityId")
//    OrderListDto modelToOrderListDto(Order order);
//
//    @Mapping(target = "orderId", source = "entityId")
//    List<OrderListDto> modelsToOrderListDtos(List<Order> orders);
}
