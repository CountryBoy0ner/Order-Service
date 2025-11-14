package com.innowise.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor

public class OrderWithUserDto {
    private OrderDto order;
    private UserDto user;
}
