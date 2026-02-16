package com.innowise.OrderService.controller;

import com.innowise.OrderService.dto.OrderWithUserDto;
import com.innowise.OrderService.dto.request.*;
import com.innowise.OrderService.dto.request.OrderUpdateRequest;
import com.innowise.OrderService.service.OrderService;
import com.innowise.OrderService.service.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;
    private final SecurityService securityService;

    @PostMapping
    public ResponseEntity<OrderWithUserDto> create(
            @Valid @RequestBody OrderCreateRequest request,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestHeader("X-Roles") String rolesHeader
    ) {
        if (rolesHeader == null || !rolesHeader.contains("ADMIN")) {
            request.setUserId(currentUserId);
        }
        OrderWithUserDto created = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }



    @PutMapping("/{id}")
    public ResponseEntity<OrderWithUserDto> update(
            @PathVariable Long id,
            @Valid @RequestBody OrderUpdateRequest request,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestHeader("X-Roles") String rolesHeader
    ) {
        securityService.checkOrderAccess(id, currentUserId, rolesHeader);
        OrderWithUserDto updated = orderService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderWithUserDto> patch(
            @PathVariable Long id,
            @RequestBody OrderPatchRequest request) {
        return ResponseEntity.ok(orderService.patch(id, request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderWithUserDto> getById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestHeader("X-Roles") String rolesHeader
    ) {
        securityService.checkOrderAccess(id, currentUserId, rolesHeader);

        OrderWithUserDto dto = orderService.getByIdWithUser(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(params = "ids")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderWithUserDto>> getByIds(
            @RequestParam List<Long> ids) {
        return ResponseEntity.ok(orderService.getByIdsWithUser(ids));
    }

    @GetMapping(params = "status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderWithUserDto>> getByStatus( //todo
            @RequestParam String status) {
        return ResponseEntity.ok(orderService.getAllByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestHeader("X-Roles") String rolesHeader
    ) {
        securityService.checkOrderAccess(id, currentUserId, rolesHeader);

        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

