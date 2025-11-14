package com.innowise.OrderService.controller;

import com.innowise.OrderService.dto.OrderWithUserDto;
import com.innowise.OrderService.dto.request.*;
import com.innowise.OrderService.dto.request.OrderUpdateRequest;
import com.innowise.OrderService.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {


    private  OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderWithUserDto> create(
            @Valid @RequestBody OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderWithUserDto> update(
            @PathVariable Long id,
            @Valid @RequestBody OrderUpdateRequest request) {
        return ResponseEntity.ok(orderService.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderWithUserDto> patch(
            @PathVariable Long id,
            @RequestBody OrderPatchRequest request) {
        return ResponseEntity.ok(orderService.patch(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderWithUserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getByIdWithUser(id));
    }
    @GetMapping(params = "ids")
    public ResponseEntity<List<OrderWithUserDto>> getByIds(
            @RequestParam List<Long> ids) {
        return ResponseEntity.ok(orderService.getByIdsWithUser(ids));
    }

    @GetMapping(params = "status")
    public ResponseEntity<List<OrderWithUserDto>> getByStatus(
            @RequestParam String status) {
        return ResponseEntity.ok(orderService.getAllByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();  // 204 NO CONTENT
    }

}

