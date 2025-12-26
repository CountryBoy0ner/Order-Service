package com.innowise.OrderService.kafka.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
@Configuration
public class KafkaTopicsConfig {

    @Bean
    NewTopic createOrderTopic(@Value("${app.kafka.topics.create-order}") String name) {
        return TopicBuilder.name(name).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic createPaymentTopic(@Value("${app.kafka.topics.create-payment}") String name) {
        return TopicBuilder.name(name).partitions(1).replicas(1).build();
    }
}
