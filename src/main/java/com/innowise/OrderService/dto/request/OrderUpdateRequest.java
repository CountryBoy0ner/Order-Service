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

    public Long getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
