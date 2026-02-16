package com.innowise.OrderService.repository;

import com.innowise.OrderService.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
