package com.innowise.OrderService.kafka.consumer;

import com.innowise.OrderService.excepion.type.NotFoundException;
import com.innowise.OrderService.kafka.event.CreatePaymentEvent;
import com.innowise.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "${app.kafka.topics.create-payment}")
    @Transactional
    public void handle(CreatePaymentEvent event) {
        var order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> NotFoundException.of("Order", "id", event.getOrderId()));

        order.setStatus(event.getStatus());
        orderRepository.save(order);
    }
}