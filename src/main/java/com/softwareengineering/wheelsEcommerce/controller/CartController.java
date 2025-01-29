package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.*;
import com.softwareengineering.wheelsEcommerce.repository.OrderRepository;
import com.softwareengineering.wheelsEcommerce.repository.ProductRepository;
import com.softwareengineering.wheelsEcommerce.repository.UserRepository;
import com.softwareengineering.wheelsEcommerce.service.CartService;
import com.softwareengineering.wheelsEcommerce.service.OrderService;
import com.softwareengineering.wheelsEcommerce.service.ProductService;
import com.softwareengineering.wheelsEcommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cart")
public class CartController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @ModelAttribute("cart")
    public Cart getCart(HttpSession session) {
        return cartService.getCart(session);
    }

    @PostMapping("/add/{id}")
    public String addToCart(@ModelAttribute("cart") Cart cart, @PathVariable Long id, @RequestParam int quantity, Model model) {

        cartService.addToCart(cart, id, quantity);
        return "redirect:/cart"; // Redirect to the cart view after adding an item
    }

    @GetMapping
    public String viewCart(@ModelAttribute("cart") Cart cart, Model model) {
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@ModelAttribute("cart") Cart cart, @PathVariable Long id) {
        cartService.removeFromCart(cart, id);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(@ModelAttribute("cart") Cart cart) {
        cartService.clearCart(cart);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkoutPage(@ModelAttribute("cart") Cart cart,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        if (userDetails != null) {
            User user = userService.findByUsername(userDetails.getUsername());
            model.addAttribute("user", user);
        }
        model.addAttribute("cart", cart);
        model.addAttribute("address", new Address());
        return "checkout/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("cart") Cart cart,
                           @AuthenticationPrincipal UserDetails userDetails,
                           @ModelAttribute Address address,
                           Model model) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(cart.getItems().stream().mapToDouble(CartItem::getSubtotal).sum());

        if (userDetails != null) {
            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
            order.setUser(user);
            order.setAddress(user.getAddress());
        } else {
            order.setAddress(address);
        }

        cart.getItems().forEach(cartItem -> {
            if (cartItem.getProduct() == null || cartItem.getProduct().getId() == null) {
                throw new IllegalArgumentException("Product or Product ID must not be null");
            }
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + cartItem.getProduct().getId()));
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItem.setPrice(product.getPrice());
            order.getItems().add(orderItem);
            product.setStock(product.getStock() - orderItem.getQuantity());
        });

        orderRepository.save(order);
        cart.getItems().clear();

        model.addAttribute("order", order);
        return "checkout/confirmation";
    }
}