package com.innowise.OrderService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class OrderUpdateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String status;

}
