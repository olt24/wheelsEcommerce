package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public String promotePage() {
        return "admin/promote"; // Returns the promotion page (promote.html)
    }

    @PostMapping("/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public String promoteUserToAdmin(@RequestParam Long userId, Model model) {
        try {
            userService.promoteToAdmin(userId);
            model.addAttribute("success", "User promoted to admin successfully!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/promote"; // Return to the promotion page with feedback
    }
}
