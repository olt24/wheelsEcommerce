package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Cart;
import com.softwareengineering.wheelsEcommerce.model.CartItem;
import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.repository.ProductRepository;
import com.softwareengineering.wheelsEcommerce.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private HttpSession session;

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Test
    void testGetCart_NewSession() {
        when(session.getAttribute("cart")).thenReturn(null);
        Cart retrievedCart = cartService.getCart(session);
        assertNotNull(retrievedCart);
        verify(session).setAttribute(eq("cart"), any(Cart.class));
    }

    @Test
    void testGetCart_ExistingSession() {
        when(session.getAttribute("cart")).thenReturn(cart);
        Cart retrievedCart = cartService.getCart(session);
        assertEquals(cart, retrievedCart);
    }

    @Test
    void testAddToCart_NewItem() {
        Long productId = 1L;
        int quantity = 2;
        Product product = new Product();
        product.setId(productId);
        product.setStock(10);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        cartService.addToCart(cart, productId, quantity);

        assertEquals(1, cart.getItems().size());
        assertEquals(productId, cart.getItems().get(0).getProduct().getId());
        assertEquals(quantity, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testAddToCart_ExistingItem() {
        Long productId = 1L;
        int initialQuantity = 2;
        int additionalQuantity = 3;

        Product product = new Product();
        product.setId(productId);
        product.setStock(10);

        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setQuantity(initialQuantity);
        cart.getItems().add(existingItem);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        cartService.addToCart(cart, productId, additionalQuantity);

        assertEquals(1, cart.getItems().size());
        assertEquals(initialQuantity + additionalQuantity, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testAddToCart_ProductNotFound() {
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(cart, productId, 1));
    }

    @Test
    void testRemoveFromCart() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        CartItem item = new CartItem();
        item.setProduct(product);
        cart.getItems().add(item);

        cartService.removeFromCart(cart, productId);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testClearCart() {
        cart.getItems().add(new CartItem());
        cartService.clearCart(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testGetTotal() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(25.0);
        CartItem item1 = new CartItem();
        item1.setProduct(product1);
        item1.setQuantity(2);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(30.0);
        CartItem item2 = new CartItem();
        item2.setProduct(product2);
        item2.setQuantity(1);

        cart.getItems().add(item1);
        cart.getItems().add(item2);

        double total = cartService.getTotal(cart);
        assertEquals(80.0, total);
    }
}
