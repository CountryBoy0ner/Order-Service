package com.innowise.OrderService.service;

import com.innowise.OrderService.dto.UserDto;

public interface UserClient {
    UserDto getById(Long id);
    UserDto getByEmail(String email);

}
