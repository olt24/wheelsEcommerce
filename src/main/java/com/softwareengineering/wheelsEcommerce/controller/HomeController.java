package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.service.CartService;
import com.softwareengineering.wheelsEcommerce.service.ProductService;
import com.softwareengineering.wheelsEcommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            model.addAttribute("username", authentication.getName());
        } else {
            model.addAttribute("username", null);
        }
        return "homepage";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
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

    @Autowired
    private CartService cartService;



    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        model.addAttribute("cart", cartService.getCart(session));
        return "cart";
    }

    @PostMapping("/cart/add/{productId}")
    public String addProductToCart(@PathVariable Long productId, HttpSession session) {
        Product product = productService.getProductById(productId);
        cartService.addProductToCart(session, product);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{productId}")
    public String removeProductFromCart(@PathVariable Long productId, HttpSession session) {
        Product product = productService.getProductById(productId);
        cartService.removeProductFromCart(session, product);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }
}