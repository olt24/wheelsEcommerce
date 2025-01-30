package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Order;
import com.softwareengineering.wheelsEcommerce.model.OrderItem;
import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.repository.OrderRepository;
import com.softwareengineering.wheelsEcommerce.service.OrderService;
import com.softwareengineering.wheelsEcommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    private User user;
    private Order order;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setStock(10);
        product.setName("Test Product");

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setItems(List.of(orderItem));
    }

    @Test
    void testFindOrdersByUser() {
        when(orderRepository.findByUser(user)).thenReturn(List.of(order));
        List<Order> orders = orderService.findOrdersByUser(user);
        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        List<Order> orders = orderService.getAllOrders();
        assertEquals(1, orders.size());
    }

    @Test
    void testSaveOrder_Success() {
        when(productService.getProductById(1L)).thenReturn(product);
        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.saveOrder(order);
        assertNotNull(savedOrder);
        verify(productService).reduceStock(1L, 2);
        verify(orderRepository).save(order);
    }

    @Test
    void testSaveOrder_InsufficientStock() {
        product.setStock(1);
        when(productService.getProductById(1L)).thenReturn(product);
        assertThrows(IllegalArgumentException.class, () -> orderService.saveOrder(order));
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);
        orderService.deleteOrder(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }
}
