package com.innowise.OrderService.mapper;

import com.innowise.OrderService.dto.ItemDto;
import com.innowise.OrderService.model.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toDto(Item entity);
    Item toEntity(ItemDto dto);
}
