package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Cart;
import com.softwareengineering.wheelsEcommerce.model.CartItem;
import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class CartService {

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void addToCart(Cart cart, Long productId, int quantity) {
        Product product1 = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product ID not found: " + productId));

        if (quantity > product1.getStock()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            Product product = findProductById(productId); // Implement this method to fetch the product
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }
    }

    public void removeFromCart(Cart cart, Long productId) {
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart(Cart cart) {
        cart.getItems().clear();
    }

    public double getTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    @Autowired
    private ProductRepository productRepository;

    // Other methods...

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
    }
}