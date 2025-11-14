package com.innowise.OrderService.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemCreateRequest {
    @NotNull
    private Long itemId;

    @NotNull
    @Positive
    private Integer quantity;
}
