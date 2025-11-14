package com.innowise.OrderService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.List;
@Getter
@Data
public class OrderCreateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String status; // если статус задаёт клиент, иначе можно убрать

    @NotEmpty
    private List<OrderItemCreateRequest> items;

    public Long getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItemCreateRequest> getItems() {
        return items;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setItems(List<OrderItemCreateRequest> items) {
        this.items = items;
    }
}
