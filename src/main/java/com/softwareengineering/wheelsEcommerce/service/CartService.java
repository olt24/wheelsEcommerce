package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Cart;
import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.repository.CartRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public Cart addProductToCart(HttpSession session, Product product) {
        Cart cart = getCart(session);
        cart.addProduct(product);
        return cartRepository.save(cart);
    }

    public Cart removeProductFromCart(HttpSession session, Product product) {
        Cart cart = getCart(session);
        cart.removeProduct(product);
        return cartRepository.save(cart);
    }

    public void clearCart(HttpSession session) {
        Cart cart = getCart(session);
        cart.clear();
        cartRepository.save(cart);
    }
}