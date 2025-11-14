package com.innowise.OrderService.dto.request;

import lombok.Data;

@Data
public class OrderPatchRequest {

    private Long userId;
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

