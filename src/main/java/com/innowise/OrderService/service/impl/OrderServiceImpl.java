package com.innowise.OrderService.service.impl;

import com.innowise.OrderService.dto.OrderDto;
import com.innowise.OrderService.dto.OrderWithUserDto;
import com.innowise.OrderService.dto.UserDto;
import com.innowise.OrderService.dto.request.OrderCreateRequest;
import com.innowise.OrderService.dto.request.OrderUpdateRequest;
import com.innowise.OrderService.dto.request.OrderPatchRequest;
import com.innowise.OrderService.excepion.type.BadRequestException;
import com.innowise.OrderService.excepion.type.NotFoundException;
import com.innowise.OrderService.kafka.producer.OrderEventProducer;
import com.innowise.OrderService.mapper.OrderMapper;
import com.innowise.OrderService.model.Item;
import com.innowise.OrderService.model.Order;
import com.innowise.OrderService.model.OrderItem;
import com.innowise.OrderService.repository.ItemRepository;
import com.innowise.OrderService.repository.OrderRepository;
import com.innowise.OrderService.service.OrderService;
import com.innowise.OrderService.service.UserClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderEventProducer orderEventProducer;
    private final OrderRepository repository;
    private final ItemRepository itemRepository;
    private final OrderMapper mapper;
    private final UserClient userClient;

    @Transactional
    @Override
    public OrderWithUserDto create(OrderCreateRequest request) {
        UserDto userDto = userClient.getById(request.getUserId());


        Order order = new Order();
        order.setUserId(userDto.getId());
        order.setStatus(request.getStatus());
        order.setCreationDate(LocalDateTime.now());

        List<OrderItem> orderItems = request.getItems().stream()
                .map(reqItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setQuantity(reqItem.getQuantity());
                    Item item = itemRepository.findById(reqItem.getItemId())
                            .orElseThrow(() -> NotFoundException.of(
                                    "Item", "id", reqItem.getItemId()));
                    orderItem.setItem(item);

                    return orderItem;
                })
                .toList();

        order.setOrderItems(orderItems);
        Order saved = repository.save(order);
        BigDecimal total = orderItems.stream()
                .map(oi -> BigDecimal.valueOf(oi.getItem().getPrice())
                        .multiply(BigDecimal.valueOf(oi.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderEventProducer.sendCreateOrder(
                new com.innowise.OrderService.kafka.event.CreateOrderEvent(
                        saved.getId(),
                        saved.getUserId(),
                        saved.getStatus(),
                        saved.getCreationDate(),
                        total
                )
        );

        OrderDto orderDto = mapper.toDto(saved);
        return new OrderWithUserDto(orderDto, userDto);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderWithUserDto getByIdWithUser(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Order", "id", id));
        OrderDto orderDto = mapper.toDto(order);
        UserDto userDto = userClient.getById(order.getUserId());
        return new OrderWithUserDto(orderDto, userDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderWithUserDto> getByIdsWithUser(List<Long> ids) { //todo
        List<Order> orders = repository.findAllByIdIn(ids);
        if (orders.isEmpty()) {
            throw NotFoundException.of("Orders", "ids", ids);
        }
        return orders.stream()
                .map(o -> new OrderWithUserDto(
                        mapper.toDto(o),
                        userClient.getById(o.getUserId())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderWithUserDto> getAllByStatus(String status) { //todo
        List<Order> orders = repository.findAllByStatus(status);
        if (orders.isEmpty()) {
            throw NotFoundException.of("Orders", "status", status);
        }
        return orders.stream()
                .map(order -> new OrderWithUserDto(
                        mapper.toDto(order),
                        userClient.getById(order.getUserId())
                ))
                .toList();
    }

    @Transactional
    @Override
    public OrderWithUserDto update(Long orderId, OrderUpdateRequest request) {
        Order existing = repository.findById(orderId)
                .orElseThrow(() -> NotFoundException.of("Order", "id", orderId));

        existing.setUserId(request.getUserId());
        existing.setStatus(request.getStatus());

        Order saved = repository.save(existing);
        OrderDto orderDto = mapper.toDto(saved);
        UserDto userDto = userClient.getById(saved.getUserId());

        return new OrderWithUserDto(orderDto, userDto);
    }

    @Transactional
    @Override
    public OrderWithUserDto patch(Long orderId, OrderPatchRequest patch) {
        Order existing = repository.findById(orderId)
                .orElseThrow(() -> NotFoundException.of("Order", "id", orderId));

        boolean changed = false;
        if (patch.getStatus() != null) {
            existing.setStatus(patch.getStatus());
            changed = true;
        }
        if (patch.getUserId() != null) {
            existing.setUserId(patch.getUserId());
            changed = true;
        }
        if (!changed) {
            throw BadRequestException.of("Order", "patch", "No fields provided");
        }
        Order saved = repository.save(existing);
        OrderDto orderDto = mapper.toDto(saved);
        UserDto userDto = userClient.getById(saved.getUserId());
        return new OrderWithUserDto(orderDto, userDto);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw NotFoundException.of("Order", "id", id);
        }
        repository.deleteById(id);
    }


}
