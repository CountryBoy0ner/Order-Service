package com.innowise.OrderService.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentEvent {
    private String paymentId;
    private Long orderId;
    private Long userId;
    private String status;
    private BigDecimal paymentAmount;
    private OffsetDateTime timestamp;
}
