package com.innowise.OrderService.dto.request;

import com.innowise.OrderService.dto.OrderDto;
import com.innowise.OrderService.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class OrderWithUserDto {
    private OrderDto order;
    private UserDto user;
}
