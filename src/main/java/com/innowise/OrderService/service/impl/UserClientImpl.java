package com.innowise.OrderService.service.impl;

import com.innowise.OrderService.dto.UserDto;
import com.innowise.OrderService.excepion.type.NotFoundException;
import com.innowise.OrderService.service.UserClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

    private final RestTemplate restTemplate;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    public UserDto getById(Long id) {
        String url = userServiceBaseUrl + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);

            String userId = request.getHeader("X-User-Id");
            if (userId != null) headers.set("X-User-Id", userId);
            String username = request.getHeader("X-Username");
            if (username != null) headers.set("X-Username", username);
            String roles = request.getHeader("X-Roles");
            if (roles != null) headers.set("X-Roles", roles);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        UserDto dto = restTemplate.exchange(url, HttpMethod.GET, entity, UserDto.class).getBody();

        if (dto != null) {
            dto.setAvailable(true);
        }
        System.out.println("AUTH HEADER = " + headers.getFirst(HttpHeaders.AUTHORIZATION));
        return dto;
    }


    public UserDto fallbackUser(Long id, Throwable ex) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setAvailable(false);
        return dto;
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
