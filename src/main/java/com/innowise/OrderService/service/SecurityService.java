package com.innowise.OrderService.service;

public interface SecurityService {
    void checkOrderAccess(Long orderId, Long currentUserId, String rolesHeader);
}