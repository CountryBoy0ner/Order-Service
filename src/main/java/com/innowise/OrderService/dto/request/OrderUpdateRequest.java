package com.innowise.OrderService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderUpdateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String status;

}
