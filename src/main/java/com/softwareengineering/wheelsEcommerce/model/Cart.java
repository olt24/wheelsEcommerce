package com.softwareengineering.wheelsEcommerce.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    // Default constructor
    public Cart() {}

    // Constructor with User parameter
    public Cart(User user) {
        this.user = user;
    }

    // Getters and setters

    public void addProduct(Product product) {
        // Add product to cart
    }

    public void removeProduct(Product product) {
        // Remove product from cart
    }

    public void clear() {
        // Clear cart
    }
}