package com.innowise.OrderService.kafka.producer;

import com.innowise.OrderService.kafka.event.CreateOrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, CreateOrderEvent> kafkaTemplate;

    @Value("${app.kafka.topics.create-order}")
    private String topic;

    public void sendCreateOrder(CreateOrderEvent event) {
        kafkaTemplate.send(topic, String.valueOf(event.getOrderId()), event);
    }
}
