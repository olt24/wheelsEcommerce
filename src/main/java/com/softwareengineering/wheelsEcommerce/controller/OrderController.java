package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.Order;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.repository.UserRepository;
import com.softwareengineering.wheelsEcommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAllOrders() {
        return orderService.getOrdersForAdmin();
    }

    @GetMapping("/user")
    public List<Order> getUserOrders(Principal principal) {
        String username = principal.getName();
        return orderService.getOrdersForUser(username);
    }

    @GetMapping("/guest")
    public List<Order> getGuestOrders(@RequestParam String sessionId) {
        return orderService.getOrdersForGuest(sessionId);
    }

    @PostMapping("/create")
    public Order createOrder(@RequestBody Order order, Principal principal,
                             @RequestParam(required = false) String sessionId) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        } else {
            order.setSessionId(sessionId);
        }
        return orderService.saveOrder(order);
    }
}
