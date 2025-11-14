package com.innowise.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderWithUserDto {
    private OrderDto order;
    private UserDto user;
}
