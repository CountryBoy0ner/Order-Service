package com.innowise.OrderService.mapper;

import com.innowise.OrderService.dto.OrderDto;
import com.innowise.OrderService.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    OrderDto toDto(Order entity);

    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderDto dto);
}
