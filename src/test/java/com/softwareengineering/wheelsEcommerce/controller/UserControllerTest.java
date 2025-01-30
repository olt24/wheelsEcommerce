package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.Order;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.service.OrderService;
import com.softwareengineering.wheelsEcommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        SecurityContextHolder.setContext(securityContext);
    }

    private void mockAuthenticatedUser(String username) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn(username);
        lenient().when(authentication.isAuthenticated()).thenReturn(true);
    }

    @Test
    void testUserDashboard() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        List<Order> orders = Arrays.asList(new Order(), new Order());

        mockAuthenticatedUser(username);
        when(userService.findByUsername(username)).thenReturn(user);
        when(orderService.findOrdersByUser(user)).thenReturn(orders);

        Model model = mock(Model.class);
        String view = userController.userDashboard(model);

        assertEquals("user/dashboard", view);
        verify(model).addAttribute("orders", orders);
        verify(model).addAttribute("user", user);
        verify(userService, times(1)).findByUsername(username);
        verify(orderService, times(1)).findOrdersByUser(user);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        String view = userController.updateUser(user);

        assertEquals("redirect:/user/dashboard", view);
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    void testViewOrders() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        List<Order> orders = Arrays.asList(new Order(), new Order());

        mockAuthenticatedUser(username);
        when(userService.findByUsername(username)).thenReturn(user);
        when(orderService.findOrdersByUser(user)).thenReturn(orders);

        Model model = mock(Model.class);
        String view = userController.viewOrders(model);

        assertEquals("user/orders", view);
        verify(model).addAttribute("orders", orders);
        verify(userService, times(1)).findByUsername(username);
        verify(orderService, times(1)).findOrdersByUser(user);
    }
}

