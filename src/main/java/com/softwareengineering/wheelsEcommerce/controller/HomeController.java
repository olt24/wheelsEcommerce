package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.repository.ProductRepository;
import com.softwareengineering.wheelsEcommerce.service.ProductService;
import com.softwareengineering.wheelsEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;


    @ModelAttribute
    public void addAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            model.addAttribute("username", authentication.getName());
        } else {
            model.addAttribute("username", null);
        }
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Product> latestProducts = productRepository.findTop5ByOrderByCreatedDateDesc();
        model.addAttribute("latestProducts", latestProducts);
        return "homepage";
    }

    @GetMapping("/products")
    public String productPage(@RequestParam(required = false) String sort, Model model) {
        List<Product> products;
        if ("priceAsc".equals(sort)) {
            products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        } else if ("priceDesc".equals(sort)) {
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        } else if ("nameAsc".equals(sort)) {
            products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } else if ("nameDesc".equals(sort)) {
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        } else {
            products = productRepository.findAll();
        }
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productPage(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        // Spring Security handles login; no need for logic here.
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        userService.registerUser(user);
        model.addAttribute("success", "Registration successful! Please log in.");
        return "redirect:/login";
    }
}