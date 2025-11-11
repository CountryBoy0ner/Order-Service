package com.innowise.OrderService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Status is required")
    private String status;

    private LocalDateTime creationDate;

    private List<OrderItemDto> orderItems;
}
