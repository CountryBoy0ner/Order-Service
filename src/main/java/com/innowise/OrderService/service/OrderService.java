package com.innowise.OrderService.service;


import com.innowise.OrderService.dto.OrderWithUserDto;
import com.innowise.OrderService.dto.request.OrderCreateRequest;
import com.innowise.OrderService.dto.request.OrderPatchRequest;
import com.innowise.OrderService.dto.request.OrderUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {
    public OrderWithUserDto getByIdWithUser(Long orderId);

    public List<OrderWithUserDto> getByIdsWithUser(List<Long> ids);

    public OrderWithUserDto create(OrderCreateRequest request);

    public List<OrderWithUserDto> getAllByStatus(String status);

    public OrderWithUserDto patch(Long orderId, OrderPatchRequest patch);

    public OrderWithUserDto update(Long orderId, OrderUpdateRequest request);

    void deleteById(Long id);


}
