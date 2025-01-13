package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Order;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.repository.OrderRepository;
import com.softwareengineering.wheelsEcommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Order> getOrdersForAdmin() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    public List<Order> getOrdersForGuest(String sessionId) {
        return orderRepository.findBySessionId(sessionId);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
