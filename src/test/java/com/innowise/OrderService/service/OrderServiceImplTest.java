package com.innowise.OrderService.service;


import com.innowise.OrderService.dto.OrderDto;
import com.innowise.OrderService.dto.OrderWithUserDto;
import com.innowise.OrderService.dto.UserDto;
import com.innowise.OrderService.dto.request.OrderCreateRequest;
import com.innowise.OrderService.dto.request.OrderPatchRequest;
import com.innowise.OrderService.dto.request.OrderUpdateRequest;
import com.innowise.OrderService.dto.request.OrderItemCreateRequest;
import com.innowise.OrderService.excepion.type.BadRequestException;
import com.innowise.OrderService.excepion.type.NotFoundException;
import com.innowise.OrderService.mapper.OrderMapper;
import com.innowise.OrderService.model.Item;
import com.innowise.OrderService.model.Order;
import com.innowise.OrderService.repository.ItemRepository;
import com.innowise.OrderService.repository.OrderRepository;
import com.innowise.OrderService.kafka.producer.OrderEventProducer;
import com.innowise.OrderService.kafka.event.CreateOrderEvent;
import com.innowise.OrderService.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderEventProducer orderEventProducer;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private OrderServiceImpl service;


    private UserDto mkUser(Long id) {
        UserDto u = new UserDto();
        u.setId(id);
        u.setEmail("user" + id + "@mail.com");
        u.setName("User" + id);
        return u;
    }

    private Item mkItem(Long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("Item" + id);
        item.setPrice(10.0);
        return item;
    }

    private Order mkOrder(Long id, Long userId, String status) {
        Order o = new Order();
        o.setId(id);
        o.setUserId(userId);
        o.setStatus(status);
        o.setCreationDate(LocalDateTime.now());
        return o;
    }

    private OrderDto mkOrderDto(Long id, Long userId, String status) {
        OrderDto dto = new OrderDto();
        dto.setId(id);
        dto.setUserId(userId);
        dto.setStatus(status);
        dto.setCreationDate(LocalDateTime.now());
        return dto;
    }


    @Test
    @DisplayName("create: saves order with items and returns OrderWithUserDto")
    void create_success() {
        // given
        Long userId = 1L;
        Long itemId = 10L;

        OrderCreateRequest req = new OrderCreateRequest();
        req.setUserId(userId);
        req.setStatus("NEW");


        doNothing().when(orderEventProducer).sendCreateOrder(any(CreateOrderEvent.class));


        OrderItemCreateRequest reqItem = new OrderItemCreateRequest();
        reqItem.setItemId(itemId);
        reqItem.setQuantity(2);
        req.setItems(List.of(reqItem));

        UserDto userDto = mkUser(userId);
        when(userClient.getById(userId)).thenReturn(userDto);

        Item item = mkItem(itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Order saved = mkOrder(100L, userId, "NEW");
        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        OrderDto orderDto = mkOrderDto(100L, userId, "NEW");
        when(orderMapper.toDto(saved)).thenReturn(orderDto);

        OrderWithUserDto result = service.create(req);

        assertNotNull(result);
        assertEquals(100L, result.getOrder().getId());
        assertEquals(userId, result.getOrder().getUserId());
        assertEquals("NEW", result.getOrder().getStatus());
        assertEquals(userId, result.getUser().getId());
        verify(userClient).getById(userId);
        verify(itemRepository).findById(itemId);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).toDto(saved);
        verify(orderEventProducer).sendCreateOrder(any(CreateOrderEvent.class));

    }

    @Test
    @DisplayName("create: item not found -> NotFoundException")
    void create_itemNotFound() {
        Long userId = 1L;
        Long itemId = 10L;
        OrderCreateRequest req = new OrderCreateRequest();
        req.setUserId(userId);
        req.setStatus("NEW");
        OrderItemCreateRequest reqItem = new OrderItemCreateRequest();
        reqItem.setItemId(itemId);
        reqItem.setQuantity(2);
        req.setItems(List.of(reqItem));
        when(userClient.getById(userId)).thenReturn(mkUser(userId));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.create(req));
        verify(orderRepository, never()).save(any());
    }


    @Test
    @DisplayName("getByIdWithUser: returns OrderWithUserDto when order exists")
    void getByIdWithUser_success() {
        Long orderId = 100L;
        Long userId = 1L;
        Order order = mkOrder(orderId, userId, "NEW");
        OrderDto orderDto = mkOrderDto(orderId, userId, "NEW");
        UserDto userDto = mkUser(userId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        when(userClient.getById(userId)).thenReturn(userDto);
        OrderWithUserDto result = service.getByIdWithUser(orderId);
        assertEquals(orderId, result.getOrder().getId());
        assertEquals(userId, result.getUser().getId());
        verify(orderRepository).findById(orderId);
        verify(orderMapper).toDto(order);
        verify(userClient).getById(userId);
    }

    @Test
    @DisplayName("getByIdWithUser: NotFoundException when order missing")
    void getByIdWithUser_notFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getByIdWithUser(999L));
    }


    @Test
    @DisplayName("getByIdsWithUser: returns list when orders exist")
    void getByIdsWithUser_success() {
        Long userId1 = 1L;
        Long userId2 = 2L;
        Order o1 = mkOrder(1L, userId1, "NEW");
        Order o2 = mkOrder(2L, userId2, "PAID");
        when(orderRepository.findAllByIdIn(List.of(1L, 2L)))
                .thenReturn(List.of(o1, o2));
        when(orderMapper.toDto(o1)).thenReturn(mkOrderDto(1L, userId1, "NEW"));
        when(orderMapper.toDto(o2)).thenReturn(mkOrderDto(2L, userId2, "PAID"));
        when(userClient.getById(userId1)).thenReturn(mkUser(userId1));
        when(userClient.getById(userId2)).thenReturn(mkUser(userId2));
        List<OrderWithUserDto> result = service.getByIdsWithUser(List.of(1L, 2L));
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getOrder().getId());
        assertEquals(2L, result.get(1).getOrder().getId());
    }

    @Test
    @DisplayName("getByIdsWithUser: NotFoundException when list is empty")
    void getByIdsWithUser_empty() {
        when(orderRepository.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of());
        assertThrows(NotFoundException.class,
                () -> service.getByIdsWithUser(List.of(1L, 2L)));
    }


    @Test
    @DisplayName("getAllByStatus: returns list when orders exist")
    void getAllByStatus_success() {
        Long userId = 1L;
        Order o1 = mkOrder(1L, userId, "NEW");
        Order o2 = mkOrder(2L, userId, "NEW");
        when(orderRepository.findAllByStatus("NEW")).thenReturn(List.of(o1, o2));
        when(orderMapper.toDto(o1)).thenReturn(mkOrderDto(1L, userId, "NEW"));
        when(orderMapper.toDto(o2)).thenReturn(mkOrderDto(2L, userId, "NEW"));
        when(userClient.getById(userId)).thenReturn(mkUser(userId));
        List<OrderWithUserDto> result = service.getAllByStatus("NEW");
        assertEquals(2, result.size());
        verify(orderRepository).findAllByStatus("NEW");
    }

    @Test
    @DisplayName("getAllByStatus: NotFoundException when no orders")
    void getAllByStatus_empty() {
        when(orderRepository.findAllByStatus("NEW")).thenReturn(List.of());
        assertThrows(NotFoundException.class,
                () -> service.getAllByStatus("NEW"));
    }


    @Test
    @DisplayName("update: updates order and returns OrderWithUserDto")
    void update_success() {
        Long orderId = 1L;
        Long oldUserId = 1L;
        Long newUserId = 2L;
        Order existing = mkOrder(orderId, oldUserId, "NEW");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existing));
        OrderUpdateRequest req = new OrderUpdateRequest();
        req.setUserId(newUserId);
        req.setStatus("PAID");
        Order saved = mkOrder(orderId, newUserId, "PAID");
        when(orderRepository.save(existing)).thenReturn(saved);
        when(orderMapper.toDto(saved)).thenReturn(mkOrderDto(orderId, newUserId, "PAID"));
        when(userClient.getById(newUserId)).thenReturn(mkUser(newUserId));
        OrderWithUserDto result = service.update(orderId, req);
        assertEquals("PAID", result.getOrder().getStatus());
        assertEquals(newUserId, result.getUser().getId());
        verify(orderRepository).save(existing);
    }

    @Test
    @DisplayName("update: NotFoundException when order missing")
    void update_notFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        OrderUpdateRequest req = new OrderUpdateRequest();
        req.setUserId(2L);
        req.setStatus("PAID");
        assertThrows(NotFoundException.class, () -> service.update(1L, req));
    }


    @Test
    @DisplayName("patch: updates only non-null fields")
    void patch_success() {
        Long orderId = 1L;
        Long oldUserId = 1L;
        Long newUserId = 5L;
        Order existing = mkOrder(orderId, oldUserId, "NEW");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existing));
        OrderPatchRequest patch = new OrderPatchRequest();
        patch.setStatus("CANCELLED");
        patch.setUserId(newUserId);
        Order saved = mkOrder(orderId, newUserId, "CANCELLED");
        when(orderRepository.save(existing)).thenReturn(saved);
        when(orderMapper.toDto(saved)).thenReturn(mkOrderDto(orderId, newUserId, "CANCELLED"));
        when(userClient.getById(newUserId)).thenReturn(mkUser(newUserId));
        OrderWithUserDto result = service.patch(orderId, patch);
        assertEquals("CANCELLED", result.getOrder().getStatus());
        assertEquals(newUserId, result.getUser().getId());
    }

    @Test
    @DisplayName("patch: NotFoundException when order missing")
    void patch_notFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        OrderPatchRequest patch = new OrderPatchRequest();
        patch.setStatus("PAID");
        assertThrows(NotFoundException.class, () -> service.patch(1L, patch));
    }

    @Test
    @DisplayName("patch: BadRequestException when no fields provided")
    void patch_noFields() {
        Order existing = mkOrder(1L, 1L, "NEW");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existing));
        OrderPatchRequest empty = new OrderPatchRequest();
        assertThrows(BadRequestException.class, () -> service.patch(1L, empty));
        verify(orderRepository, never()).save(any());
    }


    @Test
    @DisplayName("deleteById: deletes when order exists")
    void delete_success() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        service.deleteById(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById: NotFoundException when order missing")
    void delete_notFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.deleteById(1L));
        verify(orderRepository, never()).deleteById(anyLong());
    }
}
