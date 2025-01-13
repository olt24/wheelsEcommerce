package com.softwareengineering.wheelsEcommerce.repository;

import com.softwareengineering.wheelsEcommerce.model.Order;
import com.softwareengineering.wheelsEcommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findBySessionId(String sessionId);
}
