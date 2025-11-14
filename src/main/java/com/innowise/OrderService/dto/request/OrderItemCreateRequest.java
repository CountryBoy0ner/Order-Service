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

    public Long getItemId() {
        return itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
