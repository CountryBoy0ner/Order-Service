package com.innowise.OrderService.mapper;

import com.innowise.OrderService.dto.OrderItemDto;
import com.innowise.OrderService.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "item.id", target = "itemId")
    OrderItemDto toDto(OrderItem entity);

    @Mapping(source = "itemId", target = "item.id")
    OrderItem toEntity(OrderItemDto dto);
}
