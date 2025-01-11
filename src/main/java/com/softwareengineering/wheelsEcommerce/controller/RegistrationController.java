package com.softwareengineering.wheelsEcommerce.controller;


import com.softwareengineering.wheelsEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        try {
            userService.registerUser(username, email, password);
            model.addAttribute("success", "Registration successful! Please log in.");
            return "login"; // Redirect to login page after successful registration
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Show the registration page with an error message
        }
    }
}
