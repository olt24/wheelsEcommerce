package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.Order;
import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.model.ProductType;
import com.softwareengineering.wheelsEcommerce.repository.ProductRepository;
import com.softwareengineering.wheelsEcommerce.service.OrderService;
import com.softwareengineering.wheelsEcommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void testAdminDashboard() {
        List<Order> mockOrders = Arrays.asList(new Order(), new Order());
        when(orderService.getAllOrders()).thenReturn(mockOrders);

        Model model = mock(Model.class);
        String view = adminController.adminDashboard(model);

        assertEquals("admin/dashboard", view);
        verify(model).addAttribute("orders", mockOrders);
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testManageProducts() {
        List<Product> mockProducts = Arrays.asList(new Product(), new Product());
        when(productService.getAllProducts()).thenReturn(mockProducts);

        Model model = mock(Model.class);
        String view = adminController.manageProducts(model);

        assertEquals("admin/products", view);
        verify(model).addAttribute("products", mockProducts);
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testAddProductForm() {
        Model model = mock(Model.class);
        String view = adminController.addProductForm(model);

        assertEquals("admin/addProduct", view);
        verify(model).addAttribute(eq("product"), any(Product.class));
        verify(model).addAttribute("action", "add");
    }

    @Test
    void testAddProduct() {
        Product product = new Product();
        product.setProductType(ProductType.RIM);
        product.setSize("18-inch");

        String view = adminController.addProduct(product);

        assertEquals("redirect:/admin/products", view);
        assertNull(product.getSize()); // Since RIM type should clear size
        verify(productService, times(1)).saveProduct(product);
    }

    @Test
    void testEditProductForm() {
        Long productId = 1L;
        Product product = new Product();
        when(productService.getProductById(productId)).thenReturn(product);

        Model model = mock(Model.class);
        String view = adminController.editProductForm(productId, model);

        assertEquals("admin/addProduct", view);
        verify(model).addAttribute("product", product);
        verify(model).addAttribute("action", "edit");
    }

    @Test
    void testEditProduct() {
        Product product = new Product();
        product.setProductType(ProductType.TIRE);
        product.setDiameter("16-inch");

        String view = adminController.editProduct(product);

        assertEquals("redirect:/admin/products", view);
        assertNull(product.getDiameter()); // Since TIRE type should clear diameter
        verify(productService, times(1)).saveProduct(product);
    }

    @Test
    void testDeleteProduct_Success() {
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);

        String view = adminController.deleteProduct(productId);

        assertEquals("redirect:/admin/products", view);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        Long productId = 2L;
        when(productRepository.existsById(productId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminController.deleteProduct(productId);
        });

        assertEquals("Product ID not found: 2", exception.getMessage());
        verify(productRepository, never()).deleteById(productId);
    }
}
