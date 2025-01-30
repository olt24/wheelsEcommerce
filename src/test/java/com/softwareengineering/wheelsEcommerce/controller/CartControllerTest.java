package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.*;
import com.softwareengineering.wheelsEcommerce.repository.OrderRepository;
import com.softwareengineering.wheelsEcommerce.repository.ProductRepository;
import com.softwareengineering.wheelsEcommerce.repository.UserRepository;
import com.softwareengineering.wheelsEcommerce.service.CartService;
import com.softwareengineering.wheelsEcommerce.service.OrderService;
import com.softwareengineering.wheelsEcommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private UserDetails userDetails;

    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cart = new Cart();
        product = new Product();
        product.setId(1L);
        product.setName("Test Tire");
        product.setPrice(100.0);
        product.setStock(10);

        when(cartService.getCart(session)).thenReturn(cart);
    }

    @Test
    void testAddToCart() {
        doNothing().when(cartService).addToCart(any(), anyLong(), anyInt());

        String view = cartController.addToCart(cart, 1L, 2, model);

        assertEquals("redirect:/cart", view);
        verify(cartService, times(1)).addToCart(cart, 1L, 2);
    }

    @Test
    void testViewCart() {
        String view = cartController.viewCart(cart, model);

        assertEquals("cart", view);
        verify(model, times(1)).addAttribute("cart", cart);
    }

    @Test
    void testRemoveFromCart() {
        String view = cartController.removeFromCart(cart, 1L);

        assertEquals("redirect:/cart", view);
        verify(cartService, times(1)).removeFromCart(cart, 1L);
    }

    @Test
    void testClearCart() {
        String view = cartController.clearCart(cart);

        assertEquals("redirect:/cart", view);
        verify(cartService, times(1)).clearCart(cart);
    }

    @Test
    void testCheckoutPage() {
        User user = new User();
        user.setUsername("testUser");

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);

        String view = cartController.checkoutPage(cart, userDetails, model);

        assertEquals("checkout/checkout", view);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("cart", cart);
    }

    @Test
    void testCheckout() {
        User user = new User();
        user.setUsername("testUser");

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Address address = new Address();
        cart.getItems().add(new CartItem());
        cart.getItems().get(0).setProduct(product);
        cart.getItems().get(0).setQuantity(2);

        String view = cartController.checkout(cart, userDetails, address, model);

        assertEquals("checkout/confirmation", view);
        verify(orderRepository, times(1)).save(any(Order.class));
        assertTrue(cart.getItems().isEmpty());
    }
}