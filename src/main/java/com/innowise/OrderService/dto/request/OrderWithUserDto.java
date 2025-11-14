package com.innowise.OrderService.dto.request;

import com.innowise.OrderService.dto.OrderDto;
import com.innowise.OrderService.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderWithUserDto {
    private OrderDto order;
    private UserDto user;
}
