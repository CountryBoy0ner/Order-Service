package com.innowise.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderWithUserDto {
    private OrderDto order;
    private UserDto user;
}
