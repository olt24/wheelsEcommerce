package com.softwareengineering.wheelsEcommerce.repository;

import com.softwareengineering.wheelsEcommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
