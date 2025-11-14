package com.innowise.OrderService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String status; // если статус задаёт клиент, иначе можно убрать

    @NotEmpty
    private List<OrderItemCreateRequest> items;
}
