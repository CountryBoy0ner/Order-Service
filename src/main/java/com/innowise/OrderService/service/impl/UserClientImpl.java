package com.innowise.OrderService.service.impl;

import com.innowise.OrderService.dto.UserDto;
import com.innowise.OrderService.excepion.type.NotFoundException;
import com.innowise.OrderService.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

    private final RestTemplate restTemplate;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;
    @Override
    public UserDto getById(Long id) {
        String url = userServiceBaseUrl + "/" + id;
        try {
            return restTemplate.getForObject(url, UserDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NotFoundException("User" + "id" + id);
        }
    }

    @Override
    public UserDto getByEmail(String email) {
        String url = userServiceBaseUrl + "/by-email?email=" + email;
        try {
            return restTemplate.getForObject(url, UserDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NotFoundException("User email " + email + " not found");
        }
    }
}
