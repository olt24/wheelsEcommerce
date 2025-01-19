package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.Order;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.service.OrderService;
import com.softwareengineering.wheelsEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<Order> orders = orderService.findOrdersByUser(user);
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        return "user/dashboard";
    }

    @PostMapping("/user/update")
    public String updateUser(User user) {
        userService.updateUser(user);
        return "redirect:/user/dashboard";
    }

    @GetMapping("/user/orders")
    public String viewOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            List<Order> orders = orderService.findOrdersByUser(user);
            model.addAttribute("orders", orders);
        }
        return "user/orders";
    }
}