package com.innowise.OrderService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class OrderItemDto {
    private Long id;

    @NotNull(message = "Item id is required")
    private Long itemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
