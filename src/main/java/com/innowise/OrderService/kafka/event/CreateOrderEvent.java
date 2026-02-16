package com.innowise.OrderService.kafka.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderEvent {
    private Long orderId;
    private Long userId;
    private String status;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
}
