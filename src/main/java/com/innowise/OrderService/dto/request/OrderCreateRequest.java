package com.innowise.OrderService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Data
public class OrderCreateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String status;

    @NotEmpty
    private List<OrderItemCreateRequest> items;

}
