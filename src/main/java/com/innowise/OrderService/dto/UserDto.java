package com.innowise.OrderService.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter

public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private boolean available;
}

