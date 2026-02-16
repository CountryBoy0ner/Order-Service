package com.innowise.OrderService.service.impl;


import com.innowise.OrderService.model.Order;
import com.innowise.OrderService.repository.OrderRepository;
import com.innowise.OrderService.service.SecurityService;
import com.innowise.OrderService.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
public class SecurityServiceImpl implements SecurityService {

    private final OrderRepository orderRepository;

    public SecurityServiceImpl(OrderService orderService, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void checkOrderAccess(Long orderId, Long currentUserId, String rolesHeader) {
        if (rolesHeader != null && rolesHeader.contains("ADMIN")) {
            return;
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id " + orderId));

        if (!currentUserId.equals(order.getUserId())) {
            throw new AccessDeniedException("You cannot access this order");
        }
    }
}